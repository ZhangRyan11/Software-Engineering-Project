package api;

import api.config.InMemoryInputConfig;
import api.config.InMemoryOutputConfig;

public class InMemoryStorageAPI implements StorageAPI {
    private InMemoryInputConfig inputConfig;
    private InMemoryOutputConfig outputConfig;

    public InMemoryStorageAPI() {
        this.inputConfig = new InMemoryInputConfig();
        this.outputConfig = new InMemoryOutputConfig();
    }

    @Override
    public StorageResponse readData(StorageRequest request) {
        if (request instanceof InMemoryInputConfig) {
            return outputConfig;
        }
        throw new IllegalArgumentException("Unsupported request type");
    }

    @Override
    public void writeData(String destination, String data) {
        outputConfig.addOutput(data);
    }

    public InMemoryInputConfig getInputConfig() {
        return inputConfig;
    }

    public InMemoryOutputConfig getOutputConfig() {
        return outputConfig;
    }
}
