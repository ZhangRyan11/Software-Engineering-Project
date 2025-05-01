package coordinator;

import java.util.ArrayList;
import java.util.List;

import api.ComputationAPI;
import api.ComputationEngineImpl;
import api.FileDataStorage;
import api.StorageAPI;

/**
 * Single-threaded implementation of the NetworkAPI.
 */
public class Coordinator extends AbstractCoordinator {
    public Coordinator() {
        super(new ComputationEngineImpl(), new FileDataStorage());
    }
    
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
