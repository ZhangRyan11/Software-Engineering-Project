package api;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OptimizedBatchProcessingFileDataStorage implements DataStorage {
    // Optimized buffer sizes - increased for better throughput
    private static final int BUFFER_SIZE = 16 * 1024 * 1024; // 16MB main buffer (increased from 8MB)
    private static final int SMALL_BUFFER_SIZE = 128 * 1024; // 128KB small buffer (increased from 64KB)
    
    // File size thresholds - adjusted based on empirical testing
    private static final int SMALL_FILE_THRESHOLD = 64 * 1024;    // 64KB
    private static final int MEDIUM_FILE_THRESHOLD = 1024 * 1024; // 1MB
    private static final int LARGE_FILE_THRESHOLD = 8 * 1024 * 1024; // 8MB (increased from 4MB)
    
    // Batch processing thresholds
    private static final int PARALLEL_THRESHOLD = 4; // Reduced for better thread utilization
    private static final int BATCH_SIZE = 8; // Increased batch size from 5 to 8 for better throughput
    
    // Maximum number of threads to use for parallel processing - optimized for I/O operations
    private static final int MAX_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 2, 16);
    
    // Cache for recently read files
    private final Map<String, String> fileContentCache = new ConcurrentHashMap<>();
    private static final int MAX_CACHE_ENTRIES = 100;
    
    // Thread pool for parallel operations - using an optimized thread pool configuration
    private final ExecutorService threadPool;
    
    // Pre-allocated buffers for various file sizes
    private final ThreadLocal<ByteBuffer> mainBuffer = ThreadLocal.withInitial(
            () -> ByteBuffer.allocateDirect(BUFFER_SIZE));
    private final ThreadLocal<ByteBuffer> smallBuffer = ThreadLocal.withInitial(
            () -> ByteBuffer.allocateDirect(SMALL_BUFFER_SIZE));
            
    // File size tracking for optimized operations
    private final Map<String, Long> fileSizeCache = new ConcurrentHashMap<>();
    
    // Constructor with optimized thread pool
    public OptimizedBatchProcessingFileDataStorage() {
        // Create a thread pool with better behavior for I/O operations
        this.threadPool = new ThreadPoolExecutor(
            MAX_THREADS / 2,           // Core threads
            MAX_THREADS,               // Max threads
            60L, TimeUnit.SECONDS,     // Keep alive time
            new LinkedBlockingQueue<>(1000), // Work queue with bounded capacity
            new ThreadPoolExecutor.CallerRunsPolicy() // When queue is full, caller thread executes the task
        );
    }

    @Override
    public String readData(String source, String[] delimiters) throws IOException {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Source path cannot be null or empty");
        }
        
        // Check cache first
        if (fileContentCache.containsKey(source)) {
            return fileContentCache.get(source);
        }
        
        Path path = Paths.get(source);
        if (!Files.exists(path)) {
            throw new IOException("Source file does not exist: " + source);
        }
        
        // Use standard Files.readString for all files - simpler and more reliable
        String content = Files.readString(path);
        
        // Cache the content if cache isn't too large
        if (fileContentCache.size() < MAX_CACHE_ENTRIES) {
            fileContentCache.put(source, content);
        }
        
        return content;
    }

    @Override
    public boolean writeData(String destination, String data) {
        try {
            if (destination == null || destination.trim().isEmpty()) {
                throw new IllegalArgumentException("Destination path cannot be null or empty");
            }
            
            if (data == null) {
                data = ""; // Handle null data gracefully
            }
            
            Path path = Paths.get(destination);
            // Ensure parent directories exist
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            
            // Cache file size for future reference
            long dataSize = data.length();
            fileSizeCache.put(destination, dataSize);
            
            // Convert to bytes once, outside any loops
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            
            // Choose optimal write strategy based on file size
            if (bytes.length > LARGE_FILE_THRESHOLD) {
                writeWithMemoryMapping(path, bytes);
            } else if (bytes.length > MEDIUM_FILE_THRESHOLD) {
                writeWithDirectBuffer(path, bytes, mainBuffer.get());
            } else if (bytes.length > SMALL_FILE_THRESHOLD) {
                // Use main buffer for medium-small files for better performance
                writeWithDirectBuffer(path, bytes, mainBuffer.get());
            } else {
                // For very small files, use the smaller buffer
                writeWithDirectBuffer(path, bytes, smallBuffer.get());
            }
            
            // Update cache if this file is already cached
            if (fileContentCache.containsKey(destination)) {
                fileContentCache.put(destination, data);
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace(); // Log the error for debugging
            return false;
        }
    }
    
    /**
     * Batch process multiple write operations efficiently using buffer pooling and parallelism
     * @param operations Map of destination -> data pairs
     * @throws IOException If an I/O error occurs
     */
    public void batchWriteData(Map<String, String> operations) throws IOException {
        if (operations == null || operations.isEmpty()) {
            return;
        }
        
        // Create all parent directories in a single pass (more efficiently)
        createDirectoriesParallel(operations.keySet());
        
        // Group files by size for optimized processing
        Map<FileSize, List<Map.Entry<String, String>>> filesBySize = categorizeFilesBySize(operations);
        
        // Prepare cache updates (batched for efficiency)
        Map<String, String> cacheUpdates = new HashMap<>();
        for (Map.Entry<String, String> entry : operations.entrySet()) {
            String destination = entry.getKey();
            String data = entry.getValue();
            
            // Update file size cache
            fileSizeCache.put(destination, data != null ? (long)data.length() : 0L);
            
            // Prepare cache updates
            if (fileContentCache.containsKey(destination)) {
                cacheUpdates.put(destination, data);
            }
        }
        
        try {
            // Process tiny files in batches with larger batch size
            List<Future<?>> tinyFileFutures = new ArrayList<>();
            List<Map.Entry<String, String>> tinyFiles = filesBySize.get(FileSize.TINY);
            processTinyFilesInBatches(tinyFiles, tinyFileFutures);
            
            // Process small files in batches
            List<Future<?>> smallFileFutures = new ArrayList<>();
            List<Map.Entry<String, String>> smallFiles = filesBySize.get(FileSize.SMALL);
            for (int i = 0; i < smallFiles.size(); i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, smallFiles.size());
                List<Map.Entry<String, String>> batch = smallFiles.subList(i, end);
                
                smallFileFutures.add(threadPool.submit(() -> {
                    try {
                        processSmallFilesBatch(batch);
                        return null;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
            }
            
            // Process medium files with direct buffer - use more threads for medium files
            List<Future<?>> mediumFileFutures = new ArrayList<>();
            List<Map.Entry<String, String>> mediumFiles = filesBySize.get(FileSize.MEDIUM);
            int mediumBatchSize = Math.max(1, BATCH_SIZE / 2);
            
            for (int i = 0; i < mediumFiles.size(); i += mediumBatchSize) {
                int end = Math.min(i + mediumBatchSize, mediumFiles.size());
                List<Map.Entry<String, String>> batch = mediumFiles.subList(i, end);
                
                mediumFileFutures.add(threadPool.submit(() -> {
                    for (Map.Entry<String, String> entry : batch) {
                        try {
                            String destination = entry.getKey();
                            String data = entry.getValue();
                            Path path = Paths.get(destination);
                            byte[] bytes = data != null ? data.getBytes(StandardCharsets.UTF_8) : new byte[0];
                            writeWithDirectBuffer(path, bytes, mainBuffer.get());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return null;
                }));
            }
            
            // Process large files with memory mapping - one thread per file due to high resource usage
            List<Future<?>> largeFileFutures = new ArrayList<>();
            for (Map.Entry<String, String> entry : filesBySize.get(FileSize.LARGE)) {
                largeFileFutures.add(threadPool.submit(() -> {
                    try {
                        String destination = entry.getKey();
                        String data = entry.getValue();
                        Path path = Paths.get(destination);
                        byte[] bytes = data != null ? data.getBytes(StandardCharsets.UTF_8) : new byte[0];
                        writeWithMemoryMapping(path, bytes);
                        return null;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
            }
            
            // Wait for tiny and small files first (they complete faster)
            waitForFutures(tinyFileFutures);
            waitForFutures(smallFileFutures);
            
            // Then wait for medium and large files
            waitForFutures(mediumFileFutures);
            waitForFutures(largeFileFutures);
            
            // Update cache in batch
            fileContentCache.putAll(cacheUpdates);
            
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw e;
        }
    }
    
    /**
     * Process tiny files in batches with combined I/O operations
     */
    private void processTinyFilesInBatches(List<Map.Entry<String, String>> tinyFiles, 
                                          List<Future<?>> futures) {
        // For very small files, use larger batches as they have minimal overhead
        int tinyBatchSize = BATCH_SIZE * 2;
        
        for (int i = 0; i < tinyFiles.size(); i += tinyBatchSize) {
            int end = Math.min(i + tinyBatchSize, tinyFiles.size());
            List<Map.Entry<String, String>> batch = tinyFiles.subList(i, end);
            
            if (!batch.isEmpty()) {
                futures.add(threadPool.submit(() -> {
                    try {
                        ByteBuffer buffer = smallBuffer.get();
                        combinedTinyFilesWrite(batch, buffer);
                        return null;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
            }
        }
    }
    
    /**
     * Efficient processing of tiny files with a single buffer
     */
    private void combinedTinyFilesWrite(List<Map.Entry<String, String>> files, ByteBuffer buffer) 
            throws IOException {
        // Prepare all byte arrays first to reduce GC pressure during I/O
        Map<String, byte[]> preparedData = new HashMap<>(files.size());
        for (Map.Entry<String, String> entry : files) {
            String data = entry.getValue() != null ? entry.getValue() : "";
            preparedData.put(entry.getKey(), data.getBytes(StandardCharsets.UTF_8));
        }
        
        // Now perform actual I/O operations
        for (Map.Entry<String, String> entry : files) {
            String destination = entry.getKey();
            byte[] bytes = preparedData.get(destination);
            Path path = Paths.get(destination);
            
            try (FileChannel channel = FileChannel.open(path, 
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE, 
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                
                buffer.clear();
                buffer.put(bytes);
                buffer.flip();
                
                // Write in a single operation with loop for any remaining data
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
            }
        }
    }
    
    /**
     * Process a batch of small files with optimized buffer usage
     */
    private void processSmallFilesBatch(List<Map.Entry<String, String>> files) throws IOException {
        // Reuse buffer across all files in batch for better performance
        ByteBuffer buffer = smallBuffer.get();
        
        for (Map.Entry<String, String> entry : files) {
            String destination = entry.getKey();
            String data = entry.getValue() != null ? entry.getValue() : "";
            Path path = Paths.get(destination);
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            
            try (FileChannel channel = FileChannel.open(path, 
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE, 
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                
                // For improved performance, use larger chunks based on buffer capacity
                int position = 0;
                int bufferCapacity = buffer.capacity();
                
                while (position < bytes.length) {
                    int remaining = bytes.length - position;
                    int chunkSize = Math.min(bufferCapacity, remaining);
                    
                    buffer.clear();
                    buffer.put(bytes, position, chunkSize);
                    buffer.flip();
                    
                    // Write buffer contents to file with loop for any remaining data
                    while (buffer.hasRemaining()) {
                        channel.write(buffer);
                    }
                    
                    position += chunkSize;
                }
            }
        }
    }
    
    /**
     * Write file using memory mapping for optimal performance with large files
     */
    private void writeWithMemoryMapping(Path path, byte[] bytes) throws IOException {
        try (FileChannel channel = FileChannel.open(path, 
                StandardOpenOption.READ, StandardOpenOption.WRITE, 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            
            if (bytes.length > 0) { // Avoid mapping empty files
                // Map the file into memory for optimized I/O
                MappedByteBuffer mappedBuffer = channel.map(
                        FileChannel.MapMode.READ_WRITE, 0, bytes.length);
                
                // Write the data to the mapped buffer - use bulk put for better performance
                mappedBuffer.put(bytes, 0, bytes.length);
                
                // Force changes to disk
                mappedBuffer.force();
            }
        }
    }
    
    /**
     * Write file using a direct buffer with optimized chunk handling
     */
    private void writeWithDirectBuffer(Path path, byte[] bytes, ByteBuffer buffer) throws IOException {
        // For zero-length files, create an empty file and return
        if (bytes.length == 0) {
            Files.write(path, new byte[0]);
            return;
        }
        
        try (FileChannel channel = FileChannel.open(path, 
                StandardOpenOption.WRITE, StandardOpenOption.CREATE, 
                StandardOpenOption.TRUNCATE_EXISTING)) {
            
            int position = 0;
            int bufferCapacity = buffer.capacity();
            
            // Use larger chunks to minimize system calls
            while (position < bytes.length) {
                int remaining = bytes.length - position;
                // Use buffer at full capacity when possible
                int chunkSize = Math.min(bufferCapacity, remaining);
                
                buffer.clear();
                buffer.put(bytes, position, chunkSize);
                buffer.flip();
                
                // Write chunk in multiple operations if needed
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                
                position += chunkSize;
            }
        }
    }
    
    /**
     * Wait for a list of futures to complete - improved error handling
     */
    private void waitForFutures(List<Future<?>> futures) throws RuntimeException {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                if (e.getCause() instanceof IOException) {
                    throw new RuntimeException(e.getCause());
                }
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * File size categories for optimized processing strategies
     */
    private enum FileSize {
        TINY, SMALL, MEDIUM, LARGE
    }
    
    /**
     * Categorize files by size for optimized processing
     */
    private Map<FileSize, List<Map.Entry<String, String>>> categorizeFilesBySize(
            Map<String, String> operations) {
        Map<FileSize, List<Map.Entry<String, String>>> result = new HashMap<>();
        result.put(FileSize.TINY, new ArrayList<>());
        result.put(FileSize.SMALL, new ArrayList<>());
        result.put(FileSize.MEDIUM, new ArrayList<>());
        result.put(FileSize.LARGE, new ArrayList<>());
        
        // Calculate total bytes for better work distribution
        long totalSize = 0;
        for (String data : operations.values()) {
            totalSize += (data != null ? data.length() : 0);
        }
        
        // Adaptively adjust thresholds for better distribution when processing large data sets
        int tinyThreshold = 4096;
        int smallThreshold = totalSize > 100_000_000 ? SMALL_FILE_THRESHOLD * 2 : SMALL_FILE_THRESHOLD;
        int mediumThreshold = totalSize > 500_000_000 ? MEDIUM_FILE_THRESHOLD * 2 : MEDIUM_FILE_THRESHOLD;
        
        for (Map.Entry<String, String> entry : operations.entrySet()) {
            String data = entry.getValue();
            int size = data != null ? data.length() : 0;
            
            if (size < tinyThreshold) { // 4KB or less
                result.get(FileSize.TINY).add(entry);
            } else if (size < smallThreshold) {
                result.get(FileSize.SMALL).add(entry);
            } else if (size < mediumThreshold) {
                result.get(FileSize.MEDIUM).add(entry);
            } else {
                result.get(FileSize.LARGE).add(entry);
            }
        }
        
        return result;
    }
    
    /**
     * Create all required directories in a single pass with parallel processing for large sets
     */
    private void createDirectoriesParallel(Iterable<String> paths) throws IOException {
        Set<Path> dirsToCreate = new HashSet<>();
        
        for (String pathStr : paths) {
            Path path = Paths.get(pathStr);
            Path parent = path.getParent();
            if (parent != null) {
                dirsToCreate.add(parent);
            }
        }
        
        // For small number of directories, create sequentially
        if (dirsToCreate.size() <= PARALLEL_THRESHOLD) {
            for (Path dir : dirsToCreate) {
                Files.createDirectories(dir);
            }
            return;
        }
        
        // For large number of directories, create in parallel
        List<Future<?>> dirFutures = new ArrayList<>();
        List<Path> dirList = new ArrayList<>(dirsToCreate);
        int batchSize = Math.max(1, dirList.size() / MAX_THREADS);
        
        for (int i = 0; i < dirList.size(); i += batchSize) {
            final int start = i;
            final int end = Math.min(i + batchSize, dirList.size());
            
            dirFutures.add(threadPool.submit(() -> {
                for (int j = start; j < end; j++) {
                    try {
                        Files.createDirectories(dirList.get(j));
                    } catch (IOException e) {
                        // If directory already exists, continue
                        if (!Files.exists(dirList.get(j))) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return null;
            }));
        }
        
        waitForFutures(dirFutures);
    }
    
    /**
     * Clear all caches
     */
    public void clearCache() {
        fileContentCache.clear();
        fileSizeCache.clear();
    }
    
    /**
     * Shutdown the thread pool when no longer needed
     */
    public void shutdown() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public void writeDataContent(String destination, String content) throws IOException {
        if (!writeData(destination, content)) {
            throw new IOException("Failed to write data to " + destination);
        }
    }
}