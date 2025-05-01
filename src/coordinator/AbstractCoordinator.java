package coordinator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import api.ComputationAPI;
import api.StorageAPI;
import api.StorageRequest;
import api.StorageRequestImpl;
import api.StorageResponse;

/**
 * Abstract base class for coordinator implementations.
 * Contains shared logic for processing computation tasks.
 */
public abstract class AbstractCoordinator implements NetworkAPI {
    
    protected final ComputationAPI computationEngine;
    protected StorageAPI dataStore;
    
    public AbstractCoordinator(ComputationAPI computationEngine, StorageAPI dataStore) {
        this.computationEngine = computationEngine;
        this.dataStore = dataStore;
    }
    
    /**
     * Reads input from a file using the data store component.
     * 
     * @param inputPath Path to the input file
     * @param delimiter Character used to separate values in the input file
     * @return List of integers read from the file
     */
    protected List<Integer> readInputFile(String inputPath, char delimiter) {
        StorageRequest request = new StorageRequestImpl(inputPath, String.valueOf(delimiter));
        StorageResponse response = dataStore.readData(request);
        return response.getNumbers();
    }
    
    /**
     * Writes computation results to the output file.
     * 
     * @param outputPath Path where results should be written
     * @param results List of results to write
     */
    protected void writeOutput(String outputPath, List<String> results) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String result : results) {
                writer.write(result);
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Processes a single number by delegating to the computation engine.
     * 
     * @param number The number to process
     * @return The processed result as a string
     */
    protected String processNumber(int number) {
        List<Integer> factors = computationEngine.findFactors(number);
        return "Result for " + number + ": " + factors;
    }
}
