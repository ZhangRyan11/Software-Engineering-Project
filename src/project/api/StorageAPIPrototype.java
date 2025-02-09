package project.api;

import project.annotations.ProcessAPIPrototype;

// Interface for prototype storage operations
@ProcessAPIPrototype
public interface StorageAPIPrototype extends StorageAPI {
    // Store a new key-value pair
    public void store(String key, String value);
    
    // Get value by key
    public String retrieve(String key);
    
    // Remove a key-value pair
    public void delete(String key);
    
    // Update existing key with new value
    public void update(String key, String value);
    
    // Remove all stored data
    public void clear();
    
    // Display all stored data
    public void print();
}
