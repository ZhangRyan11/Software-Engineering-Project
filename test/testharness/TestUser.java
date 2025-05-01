package testharness;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import coordinator.NetworkAPI;

public class TestUser {
	
	private final NetworkAPI coordinator;

	public TestUser(NetworkAPI coordinator) {
		this.coordinator = coordinator;
	}

	public boolean run(String outputPath) {
		char delimiter = ';';
		String inputPath = "test" + File.separatorChar + "testInputFile.test";
		
		// Call the coordinator to run the computation
		coordinator.startComputation(inputPath, outputPath, delimiter);
		
		// Verify the computation ran successfully
		return verifyComputationSuccess(outputPath);
	}
	
	private boolean verifyComputationSuccess(String outputPath) {
		try {
			// Check if output file exists
			File outputFile = new File(outputPath);
			if (!outputFile.exists() || outputFile.length() == 0) {
				System.err.println("Output file doesn't exist or is empty: " + outputPath);
				return false;
			}
			
			// Read the output file and verify it contains results
			String content = new String(Files.readAllBytes(Paths.get(outputPath)));
			if (!content.contains("Result for")) {
				System.err.println("Output doesn't contain expected results: " + outputPath);
				return false;
			}
			
			return true;
		} catch (IOException e) {
			System.err.println("Error verifying computation: " + e.getMessage());
			return false;
		}
	}
}