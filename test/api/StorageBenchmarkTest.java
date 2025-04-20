package api;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StorageBenchmarkTest {
    private static final int ITERATIONS = 1000;
    private static final int WARMUP_ITERATIONS = 100;
    private static final String TEST_FILE = "benchmark_test.txt";
    private static final String TEST_DATA = "Hello, this is test data\n".repeat(1000);
    
    private File testFile;
    private DataStorage original;
    private DataStorage buffered;

    @Before
    public void setup() {
        testFile = new File(TEST_FILE);
        testFile.deleteOnExit(); // Ensure cleanup even if test fails
        original = new FileDataStorage();
        buffered = new BufferedFileDataStorage();
    }

    @After
    public void cleanup() {
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void compareStoragePerformance() throws IOException {
        // Create initial test file
        Files.writeString(Path.of(TEST_FILE), "Initial string");

        System.out.println("Starting warmup...");
        // Warmup
        runBenchmark(original, WARMUP_ITERATIONS);
        runBenchmark(buffered, WARMUP_ITERATIONS);
        
        System.out.println("Starting benchmark measurement...");
        // Actual benchmark
        long originalTime = runBenchmark(original, ITERATIONS);
        long bufferedTime = runBenchmark(buffered, ITERATIONS);

        // Calculate improvement percentage
        double improvement = ((originalTime - bufferedTime) / (double) originalTime) * 100;
        
        // Log results
        System.out.println("\nBenchmark Results:");
        System.out.println("==================");
        System.out.printf("Original implementation (FileDataStorage): %dms%n", originalTime);
        System.out.printf("Buffered implementation (BufferedFileDataStorage): %dms%n", bufferedTime);
        System.out.printf("Performance improvement: %.2f%%%n", improvement);
        
        // Assert improvement meets requirement
        assertTrue(String.format("Expected at least 10%% improvement, but got %.2f%%", improvement), 
                  improvement >= 10.0);
    }

    private long runBenchmark(DataStorage storage, int iterations) throws IOException {
        long startTime = System.nanoTime(); // Use nanoTime for more precise measurements
        try {
            for (int i = 0; i < iterations; i++) {
                storage.writeData(TEST_FILE, TEST_DATA);
                String readResult = storage.readData(TEST_FILE, new String[]{});
                // Basic validation to ensure operations actually completed
                assertTrue("Data read should not be empty", readResult != null && !readResult.isEmpty());
            }
        } catch (IOException e) {
            System.err.println("Error during benchmark: " + e.getMessage());
            throw e;
        }
        return (System.nanoTime() - startTime) / 1_000_000; // Convert to milliseconds
    }
}
