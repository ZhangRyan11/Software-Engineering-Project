package testharness;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import api.ComputationEngineImpl;
import api.ComputationAPI;
import api.FileDataStorage;
import api.StorageAPI;
import coordinator.NetworkAPI;
import coordinator.MultiThreadedCoordinator;

public class TestMultiUser {
	
	private NetworkAPI coordinator;
	private MultiThreadedCoordinator multiThreadedCoordinator;
	private ComputationAPI computationEngine;
	private StorageAPI dataStore;
	
	@BeforeEach
	public void initializeComputeEngine() {
		// Create the needed components
		computationEngine = new ComputationEngineImpl();
		dataStore = new FileDataStorage();
		
		// Create the multi-threaded coordinator with the components
		multiThreadedCoordinator = new MultiThreadedCoordinator(computationEngine, dataStore);
		coordinator = multiThreadedCoordinator;
	}
	
	@AfterEach
	public void cleanup() {
		// Shut down the executor service to prevent thread leakage
		multiThreadedCoordinator.shutdown();
	}

	@Test
	public void compareMultiAndSingleThreaded() throws Exception {
		int numThreads = 4;
		List<TestUser> testUsers = new ArrayList<>();
		for (int i = 0; i < numThreads; i++) {
			testUsers.add(new TestUser(coordinator));
		}
		
		// Run single threaded
		String singleThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.singleThreadOut.tmp";
		for (int i = 0; i < numThreads; i++) {
			File singleThreadedOut = 
					new File(singleThreadFilePrefix + i);
			singleThreadedOut.deleteOnExit();
			boolean success = testUsers.get(i).run(singleThreadedOut.getCanonicalPath());
			Assert.assertTrue("Single-threaded computation failed", success);
		}
		
		// Run multi threaded
		ExecutorService threadPool = Executors.newCachedThreadPool();
		List<Future<Boolean>> results = new ArrayList<>();
		String multiThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.multiThreadOut.tmp";
		
		for (int i = 0; i < numThreads; i++) {
			File multiThreadedOut = 
					new File(multiThreadFilePrefix + i);
			multiThreadedOut.deleteOnExit();
			String multiThreadOutputPath = multiThreadedOut.getCanonicalPath();
			TestUser testUser = testUsers.get(i);
			results.add(threadPool.submit(() -> testUser.run(multiThreadOutputPath)));
		}
		
		// Check that all multi-threaded computations succeeded
		for (Future<Boolean> future : results) {
			Assert.assertTrue("Multi-threaded computation failed", future.get());
		}
		
		// Check that the output is the same for multi-threaded and single-threaded
		List<String> singleThreaded = loadAllOutput(singleThreadFilePrefix, numThreads);
		List<String> multiThreaded = loadAllOutput(multiThreadFilePrefix, numThreads);
		Assert.assertEquals(singleThreaded, multiThreaded);
	}

	private List<String> loadAllOutput(String prefix, int numThreads) throws IOException {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < numThreads; i++) {
			File multiThreadedOut = 
					new File(prefix + i);
			result.addAll(Files.readAllLines(multiThreadedOut.toPath()));
		}
		return result;
	}
}
