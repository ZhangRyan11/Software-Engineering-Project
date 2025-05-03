package testharness;

import java.io.File;

import coordinator.NetworkAPI;  // Import the NetworkAPI interface

public class TestUser {
    
    private final NetworkAPI coordinator;

    public TestUser(NetworkAPI coordinator) {
        this.coordinator = coordinator;
    }

    public void run(String outputPath) {
        char delimiter = ';';
        String inputPath = "test" + File.separatorChar + "testInputFile.test";
        
        // Call the appropriate method on the coordinator to run the compute job
        coordinator.startComputation(inputPath, outputPath, delimiter);
    }
}