package coordinator;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class OptimizedMultiThreadedCoordinator extends AbstractCoordinator {
    private static final int BATCH_SIZE = 1000;
    private final ExecutorService executorService;
    private final ConcurrentHashMap<Integer, List<Integer>> cache;
    
    public OptimizedCoordinator() {
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
        this.cache = new ConcurrentHashMap<>();
    }
    
    @Override
    public void startComputation(String inputPath, String outputPath, char delimiter) {
        try {
            List<Integer> numbers = readInputFile(inputPath, delimiter);
            List<String> results = processBatches(numbers);
            writeOutput(outputPath, results);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> processBatches(List<Integer> numbers) throws InterruptedException, ExecutionException {
        List<List<Integer>> batches = splitIntoBatches(numbers);
        
        List<CompletableFuture<List<String>>> futures = batches.stream()
            .map(batch -> CompletableFuture.supplyAsync(
                () -> processBatch(batch),
                executorService))
            .collect(Collectors.toList());

        return futures.stream()
            .map(CompletableFuture::join)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    private List<List<Integer>> splitIntoBatches(List<Integer> numbers) {
        List<List<Integer>> batches = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i += BATCH_SIZE) {
            batches.add(numbers.subList(i, Math.min(numbers.size(), i + BATCH_SIZE)));
        }
        return batches;
    }

    private List<String> processBatch(List<Integer> batch) {
        return batch.stream()
            .map(this::processWithCache)
            .collect(Collectors.toList());
    }

    private String processWithCache(int number) {
        List<Integer> factors = cache.computeIfAbsent(number, this::computeFactorsOptimized);
        return formatResult(number, factors);
    }

    private List<Integer> computeFactorsOptimized(int number) {
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

    private String formatResult(int number, List<Integer> factors) {
        return String.format("Factors of %d: %s", number, 
            factors.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" ")));
    }

    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
