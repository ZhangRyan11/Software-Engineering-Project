package coordinator;

import api.ComputationAPI;
import api.ComputationEngineImpl;
import api.FileDataStorage;
import api.StorageAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Multi-threaded implementation of the NetworkAPI.
// Uses a thread pool to process computation tasks in parallel.

public class MultiThreadedCoordinator extends AbstractCoordinator {
    private final ExecutorService executorService;

    public MultiThreadedCoordinator() {
        super(new ComputationEngineImpl(), new FileDataStorage());
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void startComputation(String inputPath, String outputPath, char delimiter) {
        try {
            // Read input
            List<Integer> numbers = readInputFile(inputPath, delimiter);

            // Create tasks for processing each number
            List<Callable<String>> tasks = new ArrayList<>();
            for (int number : numbers) {
                tasks.add(() -> processNumber(number));
            }

            // Execute tasks in parallel
            List<Future<String>> futures = executorService.invokeAll(tasks);

            // Collect results
            List<String> results = new ArrayList<>();
            for (Future<String> future : futures) {
                results.add(future.get());
            }

            // Write output
            writeOutput(outputPath, results);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Shuts down the executor
    // Call on this method when the coordinator is no longer needed.

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
