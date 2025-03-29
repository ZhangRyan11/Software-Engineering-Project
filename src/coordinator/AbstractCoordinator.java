package coordinator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for coordinator implementations.
 * Contains shared logic for processing computation tasks.
 */
public abstract class AbstractCoordinator implements NetworkAPI {
    
    /**
     * Reads input from a file and returns a list of integers.
     * 
     * @param inputPath Path to the input file
     * @param delimiter Character used to separate values in the input file
     * @return List of integers read from the file
     */
    protected List<Integer> readInputFile(String inputPath, char delimiter) {
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
