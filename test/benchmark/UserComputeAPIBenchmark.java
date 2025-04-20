package benchmark;


import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import api.OptimizedUserComputeAPI;
import api.PrototypeUserComputeAPI;
import api.UserComputeAPI;

public class UserComputeAPIBenchmark {
    private static final int ITERATIONS = 100;
    private static final int WARMUP_ITERATIONS = 10;
    private static final int MEASUREMENTS = 1;
    private static final double IMPROVEMENT_TARGET = 0.90; // 90% of original time = 10% improvement

    @Test
    public void benchmarkComparison() {
        System.out.println("\n=== PERFORMANCE BENCHMARK ===");
        UserComputeAPI original = new PrototypeUserComputeAPI();
        UserComputeAPI optimized = new OptimizedUserComputeAPI();
        
        System.out.println("Warming up implementations...");
        warmup(original);
        warmup(optimized);
        
        List<Long> originalTimes = new ArrayList<>();
        List<Long> optimizedTimes = new ArrayList<>();
        
        for (int m = 0; m < MEASUREMENTS; m++) {
            long originalTime = measurePerformance(original);
            long optimizedTime = measurePerformance(optimized);
            
            originalTimes.add(originalTime);
            optimizedTimes.add(optimizedTime);
            
            double actualImprovement = ((double)(originalTime - optimizedTime) / originalTime) * 100;
            
            System.out.println("\nMeasurement " + (m + 1) + ":");
            System.out.println("Original:  " + originalTime + "ms");
            System.out.println("Optimized: " + optimizedTime + "ms"); 
            System.out.println("Improvement: " + actualImprovement + "%");
        }
        
        double avgOriginal = originalTimes.stream().mapToLong(l -> l).average().orElse(0);
        double avgOptimized = optimizedTimes.stream().mapToLong(l -> l).average().orElse(0);
        double actualPercentImprovement = ((avgOriginal - avgOptimized) / avgOriginal) * 100;
        
        System.out.println("\n=== FINAL RESULTS ===");
        System.out.println("Average Original:  " + avgOriginal + "ms");
        System.out.println("Average Optimized: " + avgOptimized + "ms");
        System.out.println("Actual Improvement: " + actualPercentImprovement + "%");
        System.out.println("Target Improvement: " + ((1 - IMPROVEMENT_TARGET) * 100) + "%");
        System.out.println("============================\n");
        
        assertTrue(avgOptimized/avgOriginal <= IMPROVEMENT_TARGET, 
            String.format("Required 10%% improvement, actual improvement: %.3f%%", actualPercentImprovement));
    }
    
    private void warmup(UserComputeAPI api) {
        for(int i = 0; i < WARMUP_ITERATIONS; i++) {
            try {
                api.setInputSource("warmup");
                api.setOutputDestination("warmup");
                api.setDelimiters(",");
                api.processRequest();
            } catch (Exception ignored) {
                // Exceptions during warmup can be ignored as we're only
                // interested in JVM optimization, not functional correctness
            }
        }
    }
    
    private long measurePerformance(UserComputeAPI api) {
        long start = System.nanoTime();
        
        for(int i = 0; i < ITERATIONS; i++) {
            try {
                api.setInputSource("input" + (i % 10));
                api.setOutputDestination("output" + (i % 10));
                api.setDelimiters(",");
                api.processRequest();
            } catch (Exception ignored) {
                // Exceptions during benchmarking are expected and don't affect
                // the timing measurements which is our primary concern
            }
        }
        
        return (System.nanoTime() - start) / 1_000_000;
    }
}
