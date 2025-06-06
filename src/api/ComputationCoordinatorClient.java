package api;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import coordinatorservice.ComputationCoordinatorGrpc;
import coordinatorservice.ComputationResponse;
import coordinatorservice.FileRequest;
import coordinatorservice.NumberListRequest;
import coordinatorservice.StatusRequest;
import coordinatorservice.StatusResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

// Client application that connects to the ComputationCoordinator service.
public class ComputationCoordinatorClient {
    private static final Logger logger = Logger.getLogger(ComputeCoordinatorClient.class.getName());

    private ManagedChannel channel;
    private ComputationCoordinatorGrpc.ComputationCoordinatorBlockingStub blockingStub;

    // Initialize the client with specified server coordinates
    public void init(String host, int port) {
        // Initialize gRPC channel with plain text (no encryption)
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = ComputationCoordinatorGrpc.newBlockingStub(channel);
    }

    // Shuts down the gRPC channel, terminating all ongoing RPC calls.
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    // Submits a list of numbers for computation processing using blocking stub
    public String submitNumberList(List<Double> numbers, String outputFile, String delimiter) {
        logger.info("Submitting number list...");

        // Build the request with all parameters
        NumberListRequest request = NumberListRequest.newBuilder()
                .addAllNumbers(numbers)
                .setOutputFile(outputFile)
                .setDelimiter(delimiter)
                .build();

        try {
            // Make a synchronous call
            ComputationResponse response = blockingStub.submitNumberList(request);
            
            if (response.getSuccess()) {
                logger.info("Job submitted successfully with ID: " + response.getJobId());
                return response.getJobId();
            } else {
                logger.warning("Failed to submit job: " + response.getMessage());
                return null;
            }
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null;
        }
    }

    // Submits a file containing numbers for computation processing using blocking stub
    public String submitFile(String filePath, String outputFile, String delimiter) {
        logger.info("Submitting file for computation: " + filePath);

        FileRequest request = FileRequest.newBuilder()
                .setFilePath(filePath)
                .setOutputFile(outputFile)
                .setDelimiter(delimiter)
                .build();

        try {
            // Make a synchronous call
            ComputationResponse response = blockingStub.submitFile(request);

            if (response.getSuccess()) {
                logger.info("Job submitted successfully with ID: " + response.getJobId());
                return response.getJobId();
            } else {
                logger.warning("Failed to submit job: " + response.getMessage());
                return null;
            }
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null;
        }
    }

    // Polls the server for job status until completion.
    // Runs in a separate thread to avoid blocking the main thread.
    // @param jobId The ID of the job to monitor
    private void pollJobStatus(String jobId) {
        // Create a new thread to handle polling
        new Thread(() -> {
            boolean completed = false;

            // Continue polling until job is completed
            while (!completed) {
                try {
                    // Wait between polling attempts to avoid overwhelming the server
                    Thread.sleep(1000);

                    // Create status request with job ID
                    StatusRequest request = StatusRequest.newBuilder()
                            .setJobId(jobId)
                            .build();

                    // Make synchronous call to check status
                    StatusResponse response = blockingStub.getStatus(request);

                    if (response.getCompleted()) {
                        if (response.getSuccess()) {
                            logger.info("Computation completed successfully!");

                            // Print results if available
                            List<Double> results = response.getResultsList();
                            if (!results.isEmpty()) {
                                System.out.println("\nResults:");
                                if (results.size() >= 1) {
                                    System.out.println("Sum: " + results.get(0));
                                }
                                if (results.size() >= 2) {
                                    System.out.println("Average: " + results.get(1));
                                }
                                if (results.size() >= 3) {
                                    System.out.println("Min: " + results.get(2));
                                }
                                if (results.size() >= 4) {
                                    System.out.println("Max: " + results.get(3));
                                }
                            }
                        } else {
                            logger.warning("Computation failed: " + response.getJobStatus().getMessage());
                        }
                        completed = true;
                    } else {
                        logger.info("Job still in progress...");
                    }

                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Polling interrupted", e);
                    break;
                } catch (StatusRuntimeException e) {
                    logger.log(Level.WARNING, "Error checking job status", e);
                    break;
                }
            }
        }).start();
    }

    // Main method for running end-to-end tests with both file and in-memory inputs
    public static void main(String[] args) throws Exception {
        ComputationCoordinatorClient client = new ComputationCoordinatorClient();
        
        try {
            // Connect to the computation coordinator server
            client.init("localhost", 50052);
            
            // Test 1: Submit in-memory number list
            System.out.println("TEST 1: IN-MEMORY NUMBER LIST");
            List<Double> numbers = new java.util.ArrayList<>();
            numbers.add(5.0);
            numbers.add(10.0);
            numbers.add(15.0);
            numbers.add(20.0);
            numbers.add(25.0);
            
            String outputFile1 = "in-memory-test-output.txt";
            String jobId1 = client.submitNumberList(numbers, outputFile1, ",");
            if (jobId1 != null) {
                client.pollJobStatus(jobId1);
            }
            
            // Wait a bit for the first test to complete
            Thread.sleep(5000);
            
            // Test 2: Submit file input
            System.out.println("\nTEST 2: FILE INPUT");
            String inputFile = "input.txt";
            String outputFile2 = "file-test-output.txt";
            String jobId2 = client.submitFile(inputFile, outputFile2, ",");
            if (jobId2 != null) {
                client.pollJobStatus(jobId2);
            }
            
            // Wait for test completion
            Thread.sleep(10000);
            
            // Verify results by checking if output files exist
            System.out.println("\nVERIFYING RESULTS:");
            verifyOutputFile(outputFile1);
            verifyOutputFile(outputFile2);
            
        } finally {
            // Clean up
            client.shutdown();
        }
    }
    
    private static void verifyOutputFile(String filename) {
        java.io.File file = new java.io.File(filename);
        if (file.exists()) {
            System.out.println("✅ Output file created: " + filename);
            System.out.println("   Contents: ");
            try {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(file.toPath());
                lines.forEach(line -> System.out.println("   " + line));
            } catch (Exception e) {
                System.out.println("   Unable to read file contents: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Output file not found: " + filename);
        }
    }
}
