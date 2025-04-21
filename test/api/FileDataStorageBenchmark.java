package api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Benchmark test for comparing FileDataStorage and OptimizedFileDataStorage.
 */
public class FileDataStorageBenchmark {

    private static final int TEST_DATA_SIZE = 20_000_000; // 20MB
    
    private File inputFile;
    private File standardOutputFile;
    private File optimizedOutputFile;
    private FileDataStorage standardStorage;
    private OptimizedFileDataStorage optimizedStorage;
    private String[] emptyDelimiters = new String[0];
    
    @BeforeEach
    public void setUp() throws IOException {
        // Create test files
        inputFile = File.createTempFile("benchmark-input", ".txt");
        standardOutputFile = File.createTempFile("benchmark-output-standard", ".txt");
        optimizedOutputFile = File.createTempFile("benchmark-output-optimized", ".txt");
        
        // Generate test data
        generateTestData(inputFile, TEST_DATA_SIZE);
        
        // Set up storage implementations
        standardStorage = new FileDataStorage();
        optimizedStorage = new OptimizedFileDataStorage();
        
        // Warm up the JVM
        warmUp();
    }
    
    private void warmUp() throws IOException {
        // Create a small file for warm-up
        File warmupFile = File.createTempFile("warmup", ".txt");
        StringBuilder sb = new StringBuilder(10000);
        for (int i = 0; i < 10000; i++) {
            sb.append('a');
        }
        Files.writeString(warmupFile.toPath(), sb.toString());
        
        // Warm up read operations
        for (int i = 0; i < 5; i++) {
            standardStorage.readData(warmupFile.getAbsolutePath(), emptyDelimiters);
            optimizedStorage.readData(warmupFile.getAbsolutePath(), emptyDelimiters);
        }
        
        // Warm up write operations
        String warmupData = "Warm-up data";
        for (int i = 0; i < 5; i++) {
            standardStorage.writeData(warmupFile.getAbsolutePath(), warmupData);
            optimizedStorage.writeData(warmupFile.getAbsolutePath(), warmupData);
        }
        
        // Clean up
        warmupFile.delete();
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test files
        inputFile.delete();
        standardOutputFile.delete();
        optimizedOutputFile.delete();
    }
    
    @Test
    public void benchmarkLargeFileRead() throws IOException {
        System.out.println("\nBenchmarking large file read...");
        
        double standardTime = measureReadPerformance(
            standardStorage, inputFile.getAbsolutePath());
        double optimizedTime = measureReadPerformance(
            optimizedStorage, inputFile.getAbsolutePath());
        
        System.out.println("Standard implementation: " + standardTime + " ms");
        System.out.println("Optimized implementation: " + optimizedTime + " ms");
        
        double improvement = calculateImprovement(standardTime, optimizedTime);
        System.out.println("Improvement: " + improvement + "%");
        
    }
    
    @Test
    public void benchmarkLargeFileWrite() throws IOException {
        System.out.println("\nBenchmarking large file write...");
        
        // Read large file content to write
        String largeContent = Files.readString(inputFile.toPath());
        
        double standardTime = measureWritePerformance(
            standardStorage, standardOutputFile.getAbsolutePath(), largeContent);
        double optimizedTime = measureWritePerformance(
            optimizedStorage, optimizedOutputFile.getAbsolutePath(), largeContent);
        
        System.out.println("Standard implementation: " + standardTime + " ms");
        System.out.println("Optimized implementation: " + optimizedTime + " ms");
        
        double improvement = calculateImprovement(standardTime, optimizedTime);
        System.out.println("Improvement: " + improvement + "%");
        
        // Verify files are identical
        String standardContent = Files.readString(standardOutputFile.toPath());
        String optimizedContent = Files.readString(optimizedOutputFile.toPath());
        assertEquals(standardContent, optimizedContent, "File contents must be identical");
    }
    
    private double measureReadPerformance(DataStorage storage, String filePath) throws IOException {
        final int iterations = 10;
        
        // Clear OS file cache by forcing GC and waiting
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Restore the interrupt status
            Thread.currentThread().interrupt();
        }
        
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            storage.readData(filePath, emptyDelimiters);
        }
        long endTime = System.nanoTime();
        
        return (endTime - startTime) / 1_000_000.0 / iterations; // Average in milliseconds
    }
    
    private double measureWritePerformance(DataStorage storage, String filePath, String content) throws IOException {
        final int iterations = 5;
        
        // Clear OS file cache by forcing GC and waiting
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Restore the interrupt status
            Thread.currentThread().interrupt();
        }
        
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            storage.writeData(filePath, content);
        }
        long endTime = System.nanoTime();
        
        return (endTime - startTime) / 1_000_000.0 / iterations; // Average in milliseconds
    }
    
    private double calculateImprovement(double standardTime, double optimizedTime) {
        if (standardTime <= 0) {
            return 0; // Avoid division by zero
        }
        return ((standardTime - optimizedTime) / standardTime) * 100.0;
    }
    
    private void generateTestData(File file, int size) throws IOException {
        StringBuilder builder = new StringBuilder(size);
        Random random = new Random(42); // Fixed seed for reproducibility
        
        for (int i = 0; i < size; i++) {
            // Generate a mix of letters, numbers and line breaks for realistic content
            if (i > 0 && i % 100 == 0) {
                builder.append('\n');
            } else {
                char c = (char)(random.nextInt(26) + 'a');
                builder.append(c);
            }
        }
        
        Files.writeString(file.toPath(), builder.toString());
    }
}
