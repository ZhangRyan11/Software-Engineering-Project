package api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Benchmark test for comparing individual file operations vs. batch processing optimization.
 * Tests the performance improvement achieved by processing multiple files in a single batch operation
 * versus handling each file individually for write operations.
 */
public class BatchFileOperationsBenchmark {

    private static final int TEST_DATA_SIZE = 5_000;
    private static final int ITERATIONS = 20;
    private static final int NUM_FILES = 10;
    private static final double MIN_IMPROVEMENT = 10.0; // 10% minimum improvement
    
    private List<File> testFiles;
    private Map<String, String> batchOperations;
    private String testContent;
    private OptimizedFileDataStorage standardStorage;
    private OptimizedBatchProcessingFileDataStorage batchStorage;
    
    @BeforeEach
    public void setUp() throws IOException {
        // Generate test content
        StringBuilder sb = new StringBuilder(TEST_DATA_SIZE);
        Random random = new Random(42); // Fixed seed for reproducibility
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            sb.append((char)(random.nextInt(26) + 'a'));
        }
        testContent = sb.toString();
        
        // Create test files
        testFiles = new ArrayList<>();
        for (int i = 0; i < NUM_FILES; i++) {
            File file = File.createTempFile("batch-benchmark-" + i, ".txt");
            file.deleteOnExit();
            testFiles.add(file);
        }
        
        // Create batch operations map
        batchOperations = new HashMap<>();
        for (int i = 0; i < NUM_FILES; i++) {
            batchOperations.put("batch-" + i + ".txt", testContent);
        }
        
        // Initialize storage implementations
        standardStorage = new OptimizedFileDataStorage();
        batchStorage = new OptimizedBatchProcessingFileDataStorage();
        
        // Warm up write operations
        warmUp();
    }
    
    @AfterEach
    public void tearDown() {
        for (File file : testFiles) {
            file.delete();
        }
    }
    
    private void warmUp() throws IOException {
        // Create temporary files for warm-up
        List<File> warmupStandardFiles = new ArrayList<>();
        List<File> warmupBatchFiles = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            File standardFile = File.createTempFile("warmup-std-" + i, ".txt");
            File batchFile = File.createTempFile("warmup-batch-" + i, ".txt");
            standardFile.deleteOnExit();
            batchFile.deleteOnExit();
            warmupStandardFiles.add(standardFile);
            warmupBatchFiles.add(batchFile);
        }
        
        // Warm up standard operations
        for (File file : warmupStandardFiles) {
            standardStorage.writeData(file.getAbsolutePath(), "warmup");
        }
        
        // Warm up batch operations
        Map<String, String> warmupOps = new HashMap<>();
        for (File file : warmupBatchFiles) {
            warmupOps.put(file.getAbsolutePath(), "warmup");
        }
        batchStorage.batchWriteData(warmupOps);
        
        // Clear any caches
        batchStorage.clearCache();
        
        // Force GC
        System.gc();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Clean up warmup files
        for (File file : warmupStandardFiles) {
            file.delete();
        }
        for (File file : warmupBatchFiles) {
            file.delete();
        }
    }
    
    @Test
    public void testWritePerformance() throws IOException {
        System.out.println("===== Batch Write Performance Test =====");
        
        List<File> standardOutputFiles = new ArrayList<>();
        List<File> batchOutputFiles = new ArrayList<>();
        
        // Create output files
        for (int i = 0; i < NUM_FILES; i++) {
            File standardFile = File.createTempFile("standard-write-" + i, ".txt");
            File batchFile = File.createTempFile("batch-write-" + i, ".txt");
            standardFile.deleteOnExit();
            batchFile.deleteOnExit();
            standardOutputFiles.add(standardFile);
            batchOutputFiles.add(batchFile);
        }
        
        // Prepare batch operations
        Map<String, String> writeOperations = new HashMap<>();
        for (File file : batchOutputFiles) {
            writeOperations.put(file.getAbsolutePath(), testContent);
        }
        
        // Measure standard write operations
        long standardStart = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            for (int j = 0; j < standardOutputFiles.size(); j++) {
                standardStorage.writeData(standardOutputFiles.get(j).getAbsolutePath(), testContent);
            }
        }
        long standardEnd = System.nanoTime();
        long standardTime = (standardEnd - standardStart) / 1_000_000; // ms
        
        // Clear cache between runs
        System.gc();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Measure batch write operations
        long batchStart = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            batchStorage.batchWriteData(writeOperations);
        }
        long batchEnd = System.nanoTime();
        long batchTime = (batchEnd - batchStart) / 1_000_000; // ms
        
        // Calculate improvement
        double improvementPercent = calculateImprovement(standardTime, batchTime);
        
        System.out.println("Standard write time: " + standardTime + " ms");
        System.out.println("Batch write time: " + batchTime + " ms");
        System.out.println("Improvement: " + improvementPercent + "%");
        
        // Verify files have the same content
        for (int i = 0; i < standardOutputFiles.size(); i++) {
            String standardContent = Files.readString(standardOutputFiles.get(i).toPath());
            String batchContent = Files.readString(batchOutputFiles.get(i).toPath());
            assertEquals(standardContent, batchContent, "File contents must be identical");
        }
        
        // Assert minimum improvement
        assertTrue(improvementPercent >= MIN_IMPROVEMENT, 
                   "Expected at least " + MIN_IMPROVEMENT + "% improvement, but got " + improvementPercent + "%");
        
        // Clean up files
        for (File file : standardOutputFiles) {
            file.delete();
        }
        for (File file : batchOutputFiles) {
            file.delete();
        }
    }
    
    private double calculateImprovement(long standardTime, long optimizedTime) {
        if (standardTime <= 0) {
            return 0; // Avoid division by zero
        }
        return ((double)(standardTime - optimizedTime) / standardTime) * 100.0;
    }
}