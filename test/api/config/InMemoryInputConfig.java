package api.config;

import java.util.ArrayList;
import java.util.List;

import api.StorageAPI;
import api.StorageRequest;

public abstract class InMemoryInputConfig implements StorageAPI, StorageRequest {
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
    public String getSource() {
        return "memory";
    }
}
