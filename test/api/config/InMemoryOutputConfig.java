package api.config;

import java.util.ArrayList;
import java.util.List;

import api.StorageResponse;

public class InMemoryOutputConfig implements StorageResponse {
    private List<Integer> numbers;
    private List<String> outputs;
    private boolean success = true;

    public InMemoryOutputConfig() {
        this.numbers = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public void addOutput(String output) {
        outputs.add(output);
    }

    public List<String> getOutputs() {
        return outputs;
    }
    
    /**
     * Get the output data stored in memory
     * @return List of output data strings
     */
    public List<String> getOutputData() {
        return outputs;
    }
    
    @Override
    public List<Integer> getNumbers() {
        return numbers;
    }
    
    @Override
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
