package api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Benchmark test to compare the performance of original and optimized ComputationEngine implementations.
 * The test verifies that the optimized version is at least 10% faster than the original.
 */
public class ComputationEngineBenchmark {
    
    // Number of iterations to run for averaging performance
    private static final int BENCHMARK_ITERATIONS = 5;
    // Size of the test data set
    private static final int TEST_DATA_SIZE = 100;
    // Maximum number to test (larger numbers show bigger performance differences)
    private static final int MAX_TEST_NUMBER = 100000;
    // Required improvement percentage
    private static final double REQUIRED_IMPROVEMENT = 10.0; // 10%

    @Test
    public void benchmarkComputationEngines() {
        ComputationAPI originalEngine = new ComputationEngineImpl();
        ComputationAPI optimizedEngine = new OptimizedComputationEngineImpl();
        
        // Generate test data - large numbers will highlight the difference
        List<String> testData = generateTestData(TEST_DATA_SIZE, MAX_TEST_NUMBER);
        
        // Run benchmarks
        double originalTime = benchmarkImplementation(originalEngine, testData);
        double optimizedTime = benchmarkImplementation(optimizedEngine, testData);
        
        // Calculate improvement percentage
        double improvementPercent = ((originalTime - optimizedTime) / originalTime) * 100;
        
        System.out.println("========== BENCHMARK RESULTS ==========");
        System.out.println("Original implementation average time: " + originalTime + " ms");
        System.out.println("Optimized implementation average time: " + optimizedTime + " ms");
        System.out.println("Performance improvement: " + improvementPercent + "%");
        System.out.println("=======================================");
        
        // Verify correctness
        verifyCorrectnessOfImplementations(originalEngine, optimizedEngine, testData);
        
        // Assert that the optimized version is at least 10% faster
        assertTrue(improvementPercent >= REQUIRED_IMPROVEMENT,
                "Optimized version should be at least " + REQUIRED_IMPROVEMENT + "% faster, but was only " 
                + improvementPercent + "% faster");
    }
    
    /**
     * Benchmarks a given implementation with the provided test data.
     * Returns the average execution time in milliseconds.
     */
    private double benchmarkImplementation(ComputationAPI engine, List<String> testData) {
        long totalTime = 0;
        
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            
            // Process all test data
            for (String data : testData) {
                engine.compute(data, new String[]{","});
            }
            
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }
        
        // Convert to milliseconds and calculate average
        return (totalTime / 1_000_000.0) / BENCHMARK_ITERATIONS;
    }
    
    /**
     * Generates random test data with large numbers.
     */
    private List<String> generateTestData(int size, int maxNumber) {
        List<String> testData = new ArrayList<>();
        Random random = new Random(42); // Fixed seed for reproducibility
        
        for (int i = 0; i < size; i++) {
            // Generate random numbers with a high range to show the performance difference
            int number = random.nextInt(maxNumber) + 1000;
            testData.add(String.valueOf(number));
        }
        
        return testData;
    }
    
    /**
     * Verifies that both implementations produce the same results.
     */
    private void verifyCorrectnessOfImplementations(ComputationAPI original, ComputationAPI optimized, List<String> testData) {
        for (String data : testData) {
            ComputationResult originalResult = original.compute(data, new String[]{","});
            ComputationResult optimizedResult = optimized.compute(data, new String[]{","});
            
            // Both should succeed
            assertTrue(originalResult.isSuccess());
            assertTrue(optimizedResult.isSuccess());
            
            // Results should have the same factors
            assertEquals(
                originalResult.getFactors().size(),
                optimizedResult.getFactors().size(),
                "Number of factors should match for input: " + data
            );
            
            // Check that all factors match (they might be in different order)
            assertTrue(originalResult.getFactors().containsAll(optimizedResult.getFactors()),
                      "Factors from original should match optimized for input: " + data);
            assertTrue(optimizedResult.getFactors().containsAll(originalResult.getFactors()),
                      "Factors from optimized should match original for input: " + data);
        }
    }
}
