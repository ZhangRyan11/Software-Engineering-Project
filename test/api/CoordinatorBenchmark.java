package api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration benchmark test for the coordinator components using different computation engines.
 */
public class CoordinatorBenchmark {

    private static final int TEST_DATA_SIZE = 50;
    private static final int MAX_NUMBER = 50000;

    
    private File inputFile;
    private File originalOutputFile;
    private File optimizedOutputFile;
    private ComputationCoordinatorImpl originalCoordinator;
    private ComputationCoordinatorImpl optimizedCoordinator;
    
    @BeforeEach
    public void setUp() throws IOException {
        // Create test files
        inputFile = File.createTempFile("benchmark-input", ".txt");
        originalOutputFile = File.createTempFile("benchmark-output-original", ".txt");
        optimizedOutputFile = File.createTempFile("benchmark-output-optimized", ".txt");
        
        // Generate test data
        List<Integer> numbers = generateTestData(TEST_DATA_SIZE, MAX_NUMBER);
        writeNumbersToFile(inputFile, numbers);
        
        // Set up original coordinator with original computation engine
        ComputationAPI originalEngine = new ComputationEngineImpl();
        DataStorage storage = new FileDataStorage();
        originalCoordinator = new ComputationCoordinatorImpl(originalEngine, storage);
        
        // Set up optimized coordinator with optimized computation engine
        ComputationAPI optimizedEngine = new OptimizedComputationEngineImpl();
        optimizedCoordinator = new ComputationCoordinatorImpl(optimizedEngine, storage);
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test files
        inputFile.delete();
        originalOutputFile.delete();
        optimizedOutputFile.delete();
    }
    
    @Test
    public void benchmarkCoordinators() throws IOException {
        // Create requests for both coordinators
        ComputeRequest originalRequest = new ComputeRequestImpl(
                inputFile.getAbsolutePath(), 
                originalOutputFile.getAbsolutePath(), 
                new String[]{","}, 
                inputFile.getAbsolutePath(), 
                originalOutputFile.getAbsolutePath());
        
        ComputeRequest optimizedRequest = new ComputeRequestImpl(
                inputFile.getAbsolutePath(), 
                optimizedOutputFile.getAbsolutePath(), 
                new String[]{","}, 
                inputFile.getAbsolutePath(), 
                optimizedOutputFile.getAbsolutePath());
        
        // Benchmark original implementation
        long startOriginal = System.nanoTime();
        ComputeResult originalResult = originalCoordinator.compute(originalRequest);
        long endOriginal = System.nanoTime();
        long originalTime = endOriginal - startOriginal;
        
        // Benchmark optimized implementation
        long startOptimized = System.nanoTime();
        ComputeResult optimizedResult = optimizedCoordinator.compute(optimizedRequest);
        long endOptimized = System.nanoTime();
        long optimizedTime = endOptimized - startOptimized;
        
        // Calculate improvement
        double originalTimeMs = originalTime / 1_000_000.0;
        double optimizedTimeMs = optimizedTime / 1_000_000.0;
        double improvementPercent = ((originalTimeMs - optimizedTimeMs) / originalTimeMs) * 100;
        
        System.out.println("========== COORDINATOR BENCHMARK RESULTS ==========");
        System.out.println("Original implementation time: " + originalTimeMs + " ms");
        System.out.println("Optimized implementation time: " + optimizedTimeMs + " ms");
        System.out.println("Performance improvement: " + improvementPercent + "%");
        System.out.println("=================================================");
        
        // Verify outputs are identical (or at least have the same size)
        List<String> originalOutput = Files.readAllLines(originalOutputFile.toPath());
        List<String> optimizedOutput = Files.readAllLines(optimizedOutputFile.toPath());
        assertEquals(originalOutput.size(), optimizedOutput.size(), "Output sizes should match");
    }
    
    private List<Integer> generateTestData(int size, int maxNumber) {
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random(42); // Fixed seed for reproducibility
        
        for (int i = 0; i < size; i++) {
            // Generate random numbers in a high range to show performance differences
            numbers.add(random.nextInt(maxNumber) + 1000);
        }
        
        return numbers;
    }
    
    private void writeNumbersToFile(File file, List<Integer> numbers) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            for (Integer number : numbers) {
                writer.write(number.toString());
                writer.write("\n");
            }
        }
    }
}
