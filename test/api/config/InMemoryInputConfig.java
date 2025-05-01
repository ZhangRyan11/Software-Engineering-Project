package api.config;

import java.util.ArrayList;
import java.util.List;

import api.StorageRequest;

/**
 * In-memory implementation of StorageRequest for testing.
 */
public class InMemoryInputConfig implements StorageRequest {
    private List<Integer> inputData;

    public InMemoryInputConfig() {
        this.inputData = new ArrayList<>();
    }

    public void addInput(Integer value) {
        inputData.add(value);
    }

    public List<Integer> getInputData() {
        return inputData;
    }

    @Override
    public String getPath() {
        return "memory";  // Implementing the missing method
    }

    @Override
    public String getSource() {
        return "memory";
    }
    
    // Removed @Override annotation since this method isn't in StorageRequest interface
    public String getDelimiter() {
        return ",";
    }

    /**
     * Gets the delimiters for parsing the input.
     * 
     * @return Array of delimiter strings
     */
    @Override
    public String[] getDelimiters() {
        return new String[0]; // Return empty array as default or customize based on your needs
    }
    
    /**
     * Gets the parameters for the storage request.
     * 
     * @return Array of parameter strings
     */
    @Override
    public String[] getParams() {
        return new String[0]; // Default empty array if no specific implementation needed
    }
}
