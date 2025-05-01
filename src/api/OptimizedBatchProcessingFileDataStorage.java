package api;

import java.io.File;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Optimized implementation for batch processing of file operations.
 * Uses techniques like buffer pooling, memory mapping, content caching,
 * and parallel processing to improve performance.
 */
public class OptimizedBatchProcessingFileDataStorage {
    private static final int SMALL_FILE_THRESHOLD = 4 * 1024;      // 4KB
    private static final int MEDIUM_FILE_THRESHOLD = 32 * 1024;    // 32KB
    private static final int LARGE_FILE_THRESHOLD = 1024 * 1024;   // 1MB
    private static final int BUFFER_SIZE = 8 * 1024 * 1024;        // 8MB
    private static final int SMALL_BUFFER_SIZE = 64 * 1024;        // 64KB
    private static final int MAX_CACHE_SIZE = 100;                 // Max cache entries
    private static final int BATCH_SIZE = 5;                       // Files per batch
    
    private final Map<String, String> contentCache;
    private final ThreadLocal<ByteBuffer> bufferPool;
    private final ThreadLocal<ByteBuffer> smallBufferPool;
    private final ExecutorService executor;
    
    public OptimizedBatchProcessingFileDataStorage() {
        // Initialize thread-safe cache
        this.contentCache = new ConcurrentHashMap<>(MAX_CACHE_SIZE);
        
        // Create thread-local buffer pools for reuse
        this.bufferPool = ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(BUFFER_SIZE));
        this.smallBufferPool = ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(SMALL_BUFFER_SIZE));
        
        // Create bounded thread pool based on available processors
        int processors = Math.min(4, Runtime.getRuntime().availableProcessors());
        this.executor = Executors.newFixedThreadPool(processors);
    }
    
    /**
     * Batch read multiple files in an optimized way
     */
    public List<String> batchReadData(List<String> paths, String[] delimiters) {
        List<String> results = new ArrayList<>(paths.size());
        List<Future<String>> futures = new ArrayList<>(paths.size());
        
        // Process files in parallel using the thread pool
        for (String path : paths) {
            // Check cache first
            String cachedContent = contentCache.get(path);
            if (cachedContent != null) {
                results.add(cachedContent);
            } else {
                // Submit async task for file reading
                futures.add(executor.submit(() -> readSingleFile(path)));
            }
        }
        
        // Collect results from async operations
        for (Future<String> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                results.add("");  // Add empty string for failed reads
            }
        }
        
        return results;
    }
    
    /**
     * Batch write data to multiple files in an optimized way
     */
    public void batchWriteData(Map<String, String> writeOperations) {
        // Create all parent directories in a single pass
        createParentDirectories(writeOperations.keySet());
        
        // Process in batches to optimize resource usage
        List<Map.Entry<String, String>> entries = new ArrayList<>(writeOperations.entrySet());
        List<Future<?>> futures = new ArrayList<>();
        
        for (int i = 0; i < entries.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, entries.size());
            List<Map.Entry<String, String>> batch = entries.subList(i, end);
            
            futures.add(executor.submit(() -> {
                for (Map.Entry<String, String> entry : batch) {
                    try {
                        writeSingleFile(entry.getKey(), entry.getValue());
                    } catch (IOException ignored) {
                        // Ignore individual file errors in batch
                    }
                }
                return null;
            }));
        }
        
        // Wait for all write operations to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception ignored) {
                // Ignore exceptions
            }
        }
    }
    
    /**
     * Clear the content cache
     */
    public void clearCache() {
        contentCache.clear();
    }
    
    /**
     * Shutdown the executor service
     */
    public void shutdown() {
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Read a single file with optimized strategy based on file size
     */
    private String readSingleFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        }
        
        long fileSize = file.length();
        String content;
        
        if (fileSize < SMALL_FILE_THRESHOLD) {
            // Small files: Read directly into String
            content = Files.readString(file.toPath());
        } else if (fileSize < LARGE_FILE_THRESHOLD) {
            // Medium files: Use direct buffer
            ByteBuffer buffer = fileSize < MEDIUM_FILE_THRESHOLD ? 
                smallBufferPool.get() : bufferPool.get();
            buffer.clear();
            
            try (FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
                buffer.limit((int)Math.min(fileSize, buffer.capacity()));
                channel.read(buffer);
                buffer.flip();
                content = StandardCharsets.UTF_8.decode(buffer).toString();
            }
        } else {
            // Large files: Use memory-mapped I/O
            try (FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
                MappedByteBuffer mappedBuffer = channel.map(
                    FileChannel.MapMode.READ_ONLY, 0, fileSize);
                content = StandardCharsets.UTF_8.decode(mappedBuffer).toString();
            }
        }
        
        // Cache the content if it's not too large
        if (fileSize < MEDIUM_FILE_THRESHOLD && contentCache.size() < MAX_CACHE_SIZE) {
            contentCache.put(filePath, content);
        }
        
        return content;
    }
    
    /**
     * Write a single file with optimized strategy based on content size
     */
    private void writeSingleFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        
        if (bytes.length < SMALL_FILE_THRESHOLD) {
            // Small content: Direct write
            Files.writeString(file.toPath(), content);
        } else if (bytes.length < LARGE_FILE_THRESHOLD) {
            // Medium content: Use direct buffer
            ByteBuffer buffer = bytes.length < MEDIUM_FILE_THRESHOLD ? 
                smallBufferPool.get() : bufferPool.get();
            buffer.clear();
            buffer.put(bytes);
            buffer.flip();
            
            try (FileChannel channel = FileChannel.open(
                    file.toPath(), 
                    StandardOpenOption.WRITE, 
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                channel.write(buffer);
            }
        } else {
            // Large content: Use memory-mapped I/O
            try (FileChannel channel = FileChannel.open(
                    file.toPath(), 
                    StandardOpenOption.READ, 
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                MappedByteBuffer mappedBuffer = channel.map(
                    FileChannel.MapMode.READ_WRITE, 0, bytes.length);
                mappedBuffer.put(bytes);
            }
        }
        
        // Update cache if entry exists
        if (contentCache.containsKey(filePath)) {
            contentCache.put(filePath, content);
        }
    }
    
    /**
     * Create all parent directories in a batch to reduce filesystem operations
     */
    private void createParentDirectories(Iterable<String> paths) {
        Map<String, Boolean> processedDirs = new HashMap<>();
        
        for (String path : paths) {
            File file = new File(path);
            File parentDir = file.getParentFile();
            
            if (parentDir != null) {
                String parentPath = parentDir.getAbsolutePath();
                if (!processedDirs.containsKey(parentPath)) {
                    parentDir.mkdirs();
                    processedDirs.put(parentPath, true);
                }
            }
        }
    }
}
