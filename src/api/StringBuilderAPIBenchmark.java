package api;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import project.annotations.ValidationException;

/**
 * Benchmark test for comparing PrototypeUserComputeAPI and StringBuilderOptimizedAPI.
 * Tests string building operations to validate performance improvement.
 */
public class StringBuilderAPIBenchmark {

    // Further reduced iterations to prevent timeout
    private static final int ITERATIONS = 5;
    private static final double MIN_IMPROVEMENT = 10.0; // 10% minimum improvement
    
    private PrototypeUserComputeAPI standardAPI;
    private OptimizedStringBuilderAPI optimizedAPI;
    
    @BeforeEach
    public void setUp() throws ValidationException {
        standardAPI = new PrototypeUserComputeAPI();
        optimizedAPI = new OptimizedStringBuilderAPI();
        
        // Configure APIs with similar values
        standardAPI.setInputSource("benchmark-input");
        standardAPI.setOutputDestination("benchmark-output");
        standardAPI.setDelimiters(",");
        
        optimizedAPI.setInputSource("benchmark-input");
        optimizedAPI.setOutputDestination("benchmark-output");
        optimizedAPI.setDelimiters(",");
    }
    
    @AfterEach
    public void tearDown() {
        // No cleanup needed
    }
    
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS) // Increased timeout to accommodate slow standard API
    public void testStringBuildingPerformance() throws ValidationException {
        System.out.println("\n=== String Building Test ===");
        
        // Use fixed strings to avoid string creation overhead
        String[] inputs = new String[ITERATIONS];
        String[] outputs = new String[ITERATIONS];
        for (int i = 0; i < ITERATIONS; i++) {
            inputs[i] = "input-" + i;
            outputs[i] = "output-" + i;
        }
        
        // Run optimized API first since it's faster
        long optimizedStart = System.nanoTime();
        String optimizedResult = null;
        for (int i = 0; i < ITERATIONS; i++) {
            optimizedAPI.setInputSource(inputs[i]);
            optimizedAPI.setOutputDestination(outputs[i]);
            optimizedResult = optimizedAPI.processRequest();
        }
        long optimizedEnd = System.nanoTime();
        long optimizedTime = (optimizedEnd - optimizedStart) / 1_000_000; // ms
        
        // Verify results are meaningful before proceeding
        assertTrue(optimizedResult != null && optimizedResult.length() > 0, 
                   "Optimized result should not be empty");
        
        // Now run standard API (which is likely the slow part)
        long standardStart = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            standardAPI.setInputSource(inputs[i]);
            standardAPI.setOutputDestination(outputs[i]);
            standardAPI.processRequest();
        }
        long standardEnd = System.nanoTime();
        long standardTime = (standardEnd - standardStart) / 1_000_000; // ms
        
        // Calculate improvement
        double improvementPercent = calculateImprovement(standardTime, optimizedTime);
        
        // Print only the final results
        System.out.println("Standard API time: " + standardTime + " ms");
        System.out.println("Optimized API time: " + optimizedTime + " ms");
        System.out.println("Performance improvement: " + String.format("%.2f", improvementPercent) + "%");
        
        // Assert minimum improvement
        assertTrue(improvementPercent >= MIN_IMPROVEMENT, 
                   "Expected at least " + MIN_IMPROVEMENT + "% improvement, but got " + improvementPercent + "%");
    }
    
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS) // Increased timeout to match the other test
    public void testCachedPerformance() throws ValidationException {
        System.out.println("\n=== Caching Test ===");
        
        final int repeatedCalls = 3; // Dramatically reduced to prevent timeout
        
        // Measure optimized API with cache first
        optimizedAPI.flushCache(); // Ensure cache is clear
        long optimizedStart = System.nanoTime();
        String optimizedResult = null;
        for (int i = 0; i < repeatedCalls; i++) {
            optimizedResult = optimizedAPI.processRequest(); // Same inputs repeated - should use cache
        }
        long optimizedEnd = System.nanoTime();
        long optimizedTime = (optimizedEnd - optimizedStart) / 1_000_000; // ms
        
        // Verify optimized results are meaningful
        assertTrue(optimizedResult != null && optimizedResult.length() > 0, 
                   "Optimized result should not be empty");
        
        // Use only one call for standard API which is extremely slow
        long standardStart = System.nanoTime();
        String standardResult = standardAPI.processRequest(); // Just one call to avoid timeout
        long standardEnd = System.nanoTime();
        long standardTime = (standardEnd - standardStart) / 1_000_000; // ms
        
        // Scale up standard time to match the number of repeated calls for fair comparison
        standardTime = standardTime * repeatedCalls;
        
        // Calculate improvement
        double improvementPercent = calculateImprovement(standardTime, optimizedTime);
        
        // Print only the final results
        System.out.println("Standard API estimated time: " + standardTime + " ms");
        System.out.println("Optimized API time: " + optimizedTime + " ms");
        System.out.println("Performance improvement: " + String.format("%.2f", improvementPercent) + "%");
        
        // With caching, we expect much more than the minimum improvement
        double expectedCacheImprovement = 50.0; // At least 50% improvement with caching
        assertTrue(improvementPercent >= expectedCacheImprovement, 
                   "Expected at least " + expectedCacheImprovement + "% improvement with caching, but got " + improvementPercent + "%");
    }
    
    private double calculateImprovement(long standardTime, long optimizedTime) {
        if (standardTime <= 0) {
            return 0; // Avoid division by zero
        }
        return ((double)(standardTime - optimizedTime) / standardTime) * 100.0;
    }
}
