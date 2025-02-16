package api.config;

import java.util.ArrayList;
import java.util.List;

import api.StorageAPI;
import api.StorageRequest;

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

    public String getSource() {
        return "memory";
    }
}
