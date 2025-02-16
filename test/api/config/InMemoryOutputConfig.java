package api.config;

import java.util.ArrayList;
import java.util.List;

public class InMemoryOutputConfig implements StorageResponse {
    private List<String> outputData;

    public InMemoryOutputConfig() {
        this.outputData = new ArrayList<>();
    }

    public void addOutput(String value) {
        outputData.add(value);
    }

    public List<String> getOutputData() {
        return outputData;
    }

    @Override
    public String getDestination() {
        return "memory";
    }
}
