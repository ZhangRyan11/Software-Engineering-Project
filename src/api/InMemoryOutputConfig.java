package api;

import java.util.ArrayList;
import java.util.List;
import api.ComputationCoordinatorPrototype.OutputConfig;

/**
 * In-memory implementation of OutputConfig for testing purposes
 */
public class InMemoryOutputConfig implements OutputConfig {
    private final List<String> outputData = new ArrayList<>();
    
    // Existing methods...
    
    /**
     * Get the output data stored in memory
     * @return List of output data strings
     */
    public List<String> getOutputData() {
        return outputData;
    }
    
    /**
     * Add output data to the in-memory collection
     * @param data Output data to add
     */
    public void addOutputData(String data) {
        outputData.add(data);
    }
    
    // Other implementation methods as required...
}
