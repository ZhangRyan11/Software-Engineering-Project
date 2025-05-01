package coordinator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import api.ComputationAPI;
import api.StorageAPI;
import api.StorageRequest;
import api.StorageRequestImpl;
import api.StorageResponse;
import api.StorageResponseImpl;


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
     * Reads input from a file and returns a list of integers.
     * 
     * @param inputPath Path to the input file
     * @param delimiter Character used to separate values in the input file
     * @return List of integers read from the file
     */
    protected List<Integer> readInputFile(String inputPath, char delimiter) {

        try {
            StorageRequest request = new StorageRequestImpl(inputPath, String.valueOf(delimiter));
            String source = request.getSource();
            String[] delimiters = request.getDelimiters();
            String data = dataStore.readData(source, delimiters);
            
            // Create a StorageResponseImpl instead of directly instantiating StorageResponse
            List<Integer> parsedData = parseDataToIntegers(data);
            StorageResponse response = new StorageResponseImpl(parsedData, true);
            return response.getNumbers();
        } catch (Exception e) {
            // Fallback to direct file reading if the API fails
            return readInputFileDirectly(inputPath, delimiter);
        }
    }
    
    /**
     * Parse string data into a list of integers.
     * 
     * @param data The string data to parse
     * @return List of parsed integers
     */
    private List<Integer> parseDataToIntegers(String data) {
        List<Integer> numbers = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            String[] parts = data.split("\\s+|,");
            for (String part : parts) {
                try {
                    if (!part.trim().isEmpty()) {
                        numbers.add(Integer.parseInt(part.trim()));
                    }
                } catch (NumberFormatException ignored) {
                    // Skip non-numeric values
                }
            }
        }
        return numbers;
    }
    
    /**
     * Direct implementation of file reading as a fallback method.
     * 
     * @param inputPath Path to the input file
     * @param delimiter Character used to separate values
     * @return List of integers read from the file
     */
    private List<Integer> readInputFileDirectly(String inputPath, char delimiter) {

        List<Integer> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(String.valueOf(delimiter));
                for (String value : values) {
                    if (!value.trim().isEmpty()) {
                        numbers.add(Integer.parseInt(value.trim()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numbers;
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
     * Processes a single number and returns the result.
     * This demonstrates a computational task.
     * 
     * @param number The number to process
     * @return The processed result as a string
     */
    protected String processNumber(int number) {
        // Simple example computation: square the number
        // In a real implementation, this might be a more complex calculation
        // or it might dispatch to another component
        return "Result for " + number + ": " + (number * number);
    }
}
