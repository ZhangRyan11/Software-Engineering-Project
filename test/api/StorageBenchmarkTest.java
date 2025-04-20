package api;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class StorageBenchmarkTest {
    private static final int ITERATIONS = 1000;
    private static final String TEST_FILE = "benchmark_test.txt";
    private static final String TEST_DATA = "Hello, this is test data\n".repeat(1000);

    @Test
    public void compareStoragePerformance() throws IOException {
        DataStorage original = new FileDataStorage();
        DataStorage buffered = new BufferedFileDataStorage();

        // Warm-up
        runBenchmark(original, 10);
        runBenchmark(buffered, 10);

        // Actual benchmark
        long originalTime = runBenchmark(original, ITERATIONS);
        long bufferedTime = runBenchmark(buffered, ITERATIONS);

        double improvement = (originalTime - bufferedTime) / (double) originalTime * 100;
        System.out.printf("Original time: %dms%nBuffered time: %dms%nImprovement: %.2f%%%n",
                originalTime, bufferedTime, improvement);

        assertTrue("Performance improvement should be at least 10%", improvement >= 10.0);
    }

    private long runBenchmark(DataStorage storage, int iterations) throws IOException {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            storage.writeData(TEST_FILE, TEST_DATA);
            storage.readData(TEST_FILE, new String[]{});
        }
        return System.currentTimeMillis() - startTime;
    }
}
