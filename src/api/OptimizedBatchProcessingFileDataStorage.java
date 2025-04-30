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
import java.util.concurrent.TimeUnit;

public class OptimizedBatchProcessingFileDataStorage implements DataStorage {
    // Optimized buffer sizes
    private static final int BUFFER_SIZE = 8 * 1024 * 1024; // 8MB main buffer
    private static final int SMALL_BUFFER_SIZE = 64 * 1024; // 64KB small buffer
    
    // File size thresholds
    private static final int SMALL_FILE_THRESHOLD = 32 * 1024;    // 32KB
    private static final int MEDIUM_FILE_THRESHOLD = 1024 * 1024; // 1MB
    private static final int LARGE_FILE_THRESHOLD = 4 * 1024 * 1024; // 4MB
    
    // Batch processing thresholds
    private static final int PARALLEL_THRESHOLD = 8;
    private static final int BATCH_SIZE = 5; // Number of files to process in one batch
    
    // Maximum number of threads to use for parallel processing
    private static final int MAX_THREADS = Math.min(4, Runtime.getRuntime().availableProcessors());
    
    // Cache for recently read files
    private final Map<String, String> fileContentCache = new ConcurrentHashMap<>();
    private static final int MAX_CACHE_ENTRIES = 100;
    
    // Thread pool for parallel operations
    private final ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);
    
    // Pre-allocated buffers for various file sizes
    private final ThreadLocal<ByteBuffer> mainBuffer = ThreadLocal.withInitial(
            () -> ByteBuffer.allocateDirect(BUFFER_SIZE));
    private final ThreadLocal<ByteBuffer> smallBuffer = ThreadLocal.withInitial(
            () -> ByteBuffer.allocateDirect(SMALL_BUFFER_SIZE));
            
    // File size tracking for optimized operations
    private final Map<String, Long> fileSizeCache = new ConcurrentHashMap<>();

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
        
        String content = Files.readString(path);
        
        // Cache the content if cache isn't too large
        if (fileContentCache.size() < MAX_CACHE_ENTRIES) {
            fileContentCache.put(source, content);
        }
        
        return content;
    }

    @Override
    public void writeData(String destination, String data) throws IOException {
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
        } else {
            writeWithDirectBuffer(path, bytes, smallBuffer.get());
        }
        
        // Update cache if this file is already cached
        if (fileContentCache.containsKey(destination)) {
            fileContentCache.put(destination, data);
        }
    }
    
    /**
     * Batch process multiple read operations efficiently
     * @param sources List of source paths
     * @param delimiters Delimiters for parsing
     * @return List of file contents
     * @throws IOException If an I/O error occurs
     */
    public List<String> batchReadData(List<String> sources, String[] delimiters) throws IOException {
        List<String> results = new ArrayList<>(sources.size());
        
        for (String source : sources) {
            try {
                results.add(readData(source, delimiters));
            } catch (IOException e) {
                results.add(null); // Add null for failed reads
            }
        }
        
        return results;
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
        
        // Create all parent directories in a single pass
        createDirectories(operations.keySet());
        
        // Group files by size for optimized processing
        Map<FileSize, List<Map.Entry<String, String>>> filesBySize = categorizeFilesBySize(operations);
        
        // Update file size cache
        for (Map.Entry<String, String> entry : operations.entrySet()) {
            String data = entry.getValue();
            fileSizeCache.put(entry.getKey(), data != null ? (long)data.length() : 0L);
        }
        
        try {
            // Process tiny files in batches (aggressive combining)
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
            
            // Process medium files with direct buffer
            List<Future<?>> mediumFileFutures = new ArrayList<>();
            for (Map.Entry<String, String> entry : filesBySize.get(FileSize.MEDIUM)) {
                mediumFileFutures.add(threadPool.submit(() -> {
                    try {
                        String destination = entry.getKey();
                        String data = entry.getValue();
                        Path path = Paths.get(destination);
                        byte[] bytes = data != null ? data.getBytes(StandardCharsets.UTF_8) : new byte[0];
                        writeWithDirectBuffer(path, bytes, mainBuffer.get());
                        return null;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
            }
            
            // Process large files with memory mapping
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
            
            // Wait for all operations to complete
            waitForFutures(tinyFileFutures);
            waitForFutures(smallFileFutures);
            waitForFutures(mediumFileFutures);
            waitForFutures(largeFileFutures);
            
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
        for (int i = 0; i < tinyFiles.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, tinyFiles.size());
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
        // For very small files, we can optimize by minimizing buffer flips and system calls
        buffer.clear();
        
        for (Map.Entry<String, String> entry : files) {
            String data = entry.getValue() != null ? entry.getValue() : "";
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            Path path = Paths.get(entry.getKey());
            
            try (FileChannel channel = FileChannel.open(path, 
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE, 
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                
                buffer.clear();
                buffer.put(bytes);
                buffer.flip();
                
                // Write in a single operation
                channel.write(buffer);
                
                // Update cache if this file is already cached
                if (fileContentCache.containsKey(entry.getKey())) {
                    fileContentCache.put(entry.getKey(), data);
                }
            }
        }
    }
    
    /**
     * Process a batch of small files with optimized buffer usage
     */
    private void processSmallFilesBatch(List<Map.Entry<String, String>> files) throws IOException {
        // For small files, use a thread-local buffer for better performance
        ByteBuffer buffer = smallBuffer.get();
        
        for (Map.Entry<String, String> entry : files) {
            String destination = entry.getKey();
            String data = entry.getValue() != null ? entry.getValue() : "";
            Path path = Paths.get(destination);
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            
            try (FileChannel channel = FileChannel.open(path, 
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE, 
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                
                buffer.clear();
                int position = 0;
                while (position < bytes.length) {
                    int remaining = bytes.length - position;
                    int chunkSize = Math.min(buffer.capacity(), remaining);
                    
                    buffer.clear();
                    buffer.put(bytes, position, chunkSize);
                    buffer.flip();
                    
                    while (buffer.hasRemaining()) {
                        channel.write(buffer);
                    }
                    
                    position += chunkSize;
                }
                
                // Update cache
                if (fileContentCache.containsKey(destination)) {
                    fileContentCache.put(destination, data);
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
            
            // Map the file into memory for optimized I/O
            MappedByteBuffer mappedBuffer = channel.map(
                    FileChannel.MapMode.READ_WRITE, 0, bytes.length);
            
            // Write the data to the mapped buffer
            mappedBuffer.put(bytes);
            
            // Force changes to disk
            mappedBuffer.force();
        }
    }
    
    /**
     * Write file using a direct buffer with optimized chunk handling
     */
    private void writeWithDirectBuffer(Path path, byte[] bytes, ByteBuffer buffer) throws IOException {
        try (FileChannel channel = FileChannel.open(path, 
                StandardOpenOption.WRITE, StandardOpenOption.CREATE, 
                StandardOpenOption.TRUNCATE_EXISTING)) {
            
            int position = 0;
            while (position < bytes.length) {
                int remaining = bytes.length - position;
                int chunkSize = Math.min(buffer.capacity(), remaining);
                
                buffer.clear();
                buffer.put(bytes, position, chunkSize);
                buffer.flip();
                
                // Write chunk in a single operation if possible
                channel.write(buffer);
                
                position += chunkSize;
            }
        }
    }
    
    /**
     * Wait for a list of futures to complete
     */
    private void waitForFutures(List<Future<?>> futures) throws RuntimeException {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
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
        
        for (Map.Entry<String, String> entry : operations.entrySet()) {
            String data = entry.getValue();
            int size = data != null ? data.length() : 0;
            
            if (size < 4096) { // 4KB or less
                result.get(FileSize.TINY).add(entry);
            } else if (size < SMALL_FILE_THRESHOLD) {
                result.get(FileSize.SMALL).add(entry);
            } else if (size < MEDIUM_FILE_THRESHOLD) {
                result.get(FileSize.MEDIUM).add(entry);
            } else {
                result.get(FileSize.LARGE).add(entry);
            }
        }
        
        return result;
    }
    
    /**
     * Create all required directories in a single pass
     */
    private void createDirectories(Iterable<String> paths) throws IOException {
        Set<Path> dirsToCreate = new HashSet<>();
        
        for (String pathStr : paths) {
            Path path = Paths.get(pathStr);
            Path parent = path.getParent();
            if (parent != null) {
                dirsToCreate.add(parent);
            }
        }
        
        for (Path dir : dirsToCreate) {
            Files.createDirectories(dir);
        }
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
}
