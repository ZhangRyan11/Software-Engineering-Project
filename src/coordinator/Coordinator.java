package coordinator;

import java.util.ArrayList;
import java.util.List;

/**
 * Single-threaded implementation of the NetworkAPI.
 */
public class Coordinator extends AbstractCoordinator {
    
    @Override
    public void startComputation(String inputPath, String outputPath, char delimiter) {
        // Read input
        List<Integer> numbers = readInputFile(inputPath, delimiter);
        
        // Process each number sequentially
        List<String> results = new ArrayList<>();
        for (int number : numbers) {
            results.add(processNumber(number));
        }
        
        // Write output
        writeOutput(outputPath, results);
    }
}
