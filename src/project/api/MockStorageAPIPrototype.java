package project.api;

import java.util.HashMap;
import java.util.Map;

// Mock implementation for testing storage operations
public class MockStorageAPIPrototype implements StorageAPIPrototype {
    // In-memory storage using HashMap
    private Map<String, String> storage = new HashMap<>();

    // Read data from storage using request source as key
    @Override
    public StorageResponse readData(StorageRequest request) {
        String data = storage.getOrDefault(request.getSource(), null);
        return new StorageResponse(data, data != null);
    }

    // Write data to storage using destination as key
    @Override
    public void writeData(String destination, String data) {
        storage.put(destination, data);
    }

    // Store new key-value pair
    @Override
    public void store(String key, String value) {
        storage.put(key, value);
    }

    // Get value using key
    @Override
    public String retrieve(String key) {
        return storage.get(key);
    }

    // Remove key-value pair from storage
    @Override
    public void delete(String key) {
        storage.remove(key);
    }

    // Update value if key exists
    @Override
    public void update(String key, String value) {
        if (storage.containsKey(key)) {
            storage.put(key, value);
        }
    }

    // Clear all storage data
    @Override
    public void clear() {
        storage.clear();
    }

    // Print all storage contents
    @Override
    public void print() {
        System.out.println("Storage contents:");
        storage.forEach((key, value) -> 
            System.out.printf("Key: %s, Value: %s%n", key, value));
    }
}
