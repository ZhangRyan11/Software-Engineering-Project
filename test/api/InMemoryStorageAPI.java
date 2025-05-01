package api;

import api.config.InMemoryInputConfig;
import api.config.InMemoryOutputConfig;

public class InMemoryStorageAPI implements StorageAPI {
    public InMemoryInputConfig inputConfig;
    public InMemoryOutputConfig outputConfig;

    public InMemoryStorageAPI() {
        this.inputConfig = new InMemoryInputConfig();
        this.outputConfig = new InMemoryOutputConfig();
    }

    // Implement the required readData method
    @Override
    public String readData(String path, String[] params) {
        // Implementation that likely uses the existing code or maps to a different method
        // This might be an adaptation of your previous readData(StorageRequest) method
        return null; // Modify this according to your implementation needs
    }

    public StorageResponse readData(StorageRequest request) {
        if (request instanceof InMemoryInputConfig) {
            return outputConfig;
        }
        throw new IllegalArgumentException("Unsupported request type");
    }

    @Override
    public boolean writeData(String destination, String data) {
        try {
            outputConfig.addOutput(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public InMemoryInputConfig getInputConfig() {
        return inputConfig;
    }

    public InMemoryOutputConfig getOutputConfig() {
        return outputConfig;
    }

    public String getSource() {
        return null;
    }
}
