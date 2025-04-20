package coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Collections;
import java.util.stream.Collectors;

// Multi-threaded implementation of the NetworkAPI.
// Uses a thread pool to process computation tasks in parallel.

public class MultiThreadedCoordinator extends AbstractCoordinator {
    
    // Upper bound for the number of threads in the pool
    private static final int MAX_THREADS = 4;
    
    private final ExecutorService executorService;
    
    public MultiThreadedCoordinator() {
        // Create a fixed thread pool with a reasonable upper bound
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    private String formatResult(int number, List<Integer> factors) {
        return String.format("Factors of %d: %s", number, 
            factors.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" ")));
    }

    private List<Integer> computeFactors(int number) {
        List<Integer> factors = new ArrayList<>();
        int sqrt = (int) Math.sqrt(number);
        
        for (int i = 1; i <= sqrt; i++) {
            if (number % i == 0) {
                factors.add(i);
                if (i != number / i) {
                    factors.add(number / i);
                }
            }
        }
        Collections.sort(factors);
        return factors;
    }

    @Override
    protected String processNumber(int number) {
        return formatResult(number, computeFactors(number));
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
