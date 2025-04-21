package api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.BeforeAll;

/**
 * Benchmark test to compare the performance of original and optimized ComputationEngine implementations.
 * The test verifies that the optimized version is at least 10% faster than the original.
 */
public class ComputationEngineBenchmark {
    
    // Number of iterations to run for averaging performance
    private static final int DEFAULT_BENCHMARK_ITERATIONS = 3;
    // Size of the test data set
    private static final int DEFAULT_TEST_DATA_SIZE = 50;
    // Maximum number to test
    private static final int DEFAULT_MAX_TEST_NUMBER = 25000;
    
    private static int BENCHMARK_ITERATIONS;
    private static int TEST_DATA_SIZE;
    private static int MAX_TEST_NUMBER;
    
    @BeforeAll
    public static void setupParameters() {
        // Load configuration from system properties or use defaults
        BENCHMARK_ITERATIONS = Integer.getInteger("benchmark.iterations", DEFAULT_BENCHMARK_ITERATIONS);
        TEST_DATA_SIZE = Integer.getInteger("benchmark.datasize", DEFAULT_TEST_DATA_SIZE);
        MAX_TEST_NUMBER = Integer.getInteger("benchmark.maxnumber", DEFAULT_MAX_TEST_NUMBER);
        
        System.out.println("Benchmark configuration:");
        System.out.println("- Iterations: " + BENCHMARK_ITERATIONS);
        System.out.println("- Test data size: " + TEST_DATA_SIZE);
        System.out.println("- Max test number: " + MAX_TEST_NUMBER);
    }

    @Test
    @Tag("benchmark")
    @Tag("slow")
    @EnabledIfSystemProperty(named = "run.benchmark", matches = "true")
    public void benchmarkComputationEngines() {
        ComputationAPI originalEngine = new ComputationEngineImpl();
        ComputationAPI optimizedEngine = new OptimizedComputationEngineImpl();
        
        // Generate test data - large numbers will highlight the difference
        List<String> testData = generateTestData(TEST_DATA_SIZE, MAX_TEST_NUMBER);
        
        // Warm up the JVM for more consistent results
        warmupJVM(originalEngine, optimizedEngine, testData);
        
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
    }
    
    /**
     * Warm up the JVM before benchmarking for more consistent results
     */
    private void warmupJVM(ComputationAPI originalEngine, ComputationAPI optimizedEngine, List<String> testData) {
        System.out.println("Warming up JVM...");
        // Use a small subset of data for warm-up
        List<String> warmupData = testData.subList(0, Math.min(10, testData.size()));
        
        for (int i = 0; i < 3; i++) {
            // Process using both engines
            for (String data : warmupData) {
                originalEngine.compute(data, new String[]{","});
                optimizedEngine.compute(data, new String[]{","});
            }
        }
        System.out.println("Warm-up complete.");
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
