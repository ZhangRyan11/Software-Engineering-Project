package testharness;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
        
        // Verify computation output exists and has content
        try {
            File outputFile = new File(outputPath);
            if (!outputFile.exists()) {
                System.err.println("ERROR: Output file was not created at: " + outputPath);
            } else {
                List<String> lines = Files.readAllLines(Paths.get(outputPath));
                if (lines.isEmpty()) {
                    System.err.println("ERROR: Output file is empty: " + outputPath);
                } else {
                    System.out.println("Successfully processed computation to " + outputPath + 
                            " with " + lines.size() + " result lines");
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: Failed to verify output: " + e.getMessage());
        }
    }
}