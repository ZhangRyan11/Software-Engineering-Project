package project.impl;

import project.annotations.ProcessAPIPrototype;
import project.api.*;

public class StorageAPIPrototype implements StorageAPI {
    @ProcessAPIPrototype
    public StorageResponse readData(StorageRequest request) {
        // Prototype implementation
        return null;
    }

    @ProcessAPIPrototype
    public void writeData(String destination, String data) {
        // Prototype implementation
    }
}
