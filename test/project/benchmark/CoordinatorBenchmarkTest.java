package project.benchmark;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import coordinator.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class CoordinatorBenchmarkTest {
    private static final int TEST_SIZE = 10000;
    private static final int MAX_VALUE = 1000000;
    private static final String TEST_FILE = "benchmark_input.txt";
    private static final String OUTPUT_1 = "output1.txt";
    private static final String OUTPUT_2 = "output2.txt";

    @Test
    public void compareImplementations() throws Exception {
        generateTestData();

        // Test original implementation
        MultiThreadedCoordinator original = new MultiThreadedCoordinator();
        long startTime = System.nanoTime();
        original.startComputation(TEST_FILE, OUTPUT_1, ',');
        long originalTime = System.nanoTime() - startTime;
        original.shutdown();

        // Test optimized implementation
        OptimizedCoordinator optimized = new OptimizedCoordinator();
        startTime = System.nanoTime();
        optimized.startComputation(TEST_FILE, OUTPUT_2, ',');
        long optimizedTime = System.nanoTime() - startTime;
        optimized.shutdown();

        // Verify results match
        assertArrayEquals(
            Files.readAllBytes(Paths.get(OUTPUT_1)),
            Files.readAllBytes(Paths.get(OUTPUT_2))
        );

        // Calculate improvement
        double improvement = 100.0 * (originalTime - optimizedTime) / originalTime;
        System.out.printf("Original implementation: %.2f ms%n", originalTime / 1_000_000.0);
        System.out.printf("Optimized implementation: %.2f ms%n", optimizedTime / 1_000_000.0);
        System.out.printf("Performance improvement: %.2f%%%n", improvement);

        assertTrue(improvement >= 10.0,
            "Performance improvement must be at least 10%, but was " + improvement + "%");

        cleanup();
    }

    private void generateTestData() throws IOException {
        Random rand = new Random(42);
        try (PrintWriter writer = new PrintWriter(TEST_FILE)) {
            for (int i = 0; i < TEST_SIZE; i++) {
                writer.println(rand.nextInt(MAX_VALUE) + 1);
            }
        }
    }

    private void cleanup() {
        new File(TEST_FILE).delete();
        new File(OUTPUT_1).delete();
        new File(OUTPUT_2).delete();
    }
}
