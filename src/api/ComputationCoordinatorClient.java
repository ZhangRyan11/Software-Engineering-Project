package api;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import coordinatorservice.ComputationCoordinatorGrpc;
import coordinatorservice.ComputationCoordinatorGrpc.ComputationCoordinatorStub;
import coordinatorservice.CoordinatorServiceProto.ComputationResponse;
import coordinatorservice.CoordinatorServiceProto.FileRequest;
import coordinatorservice.CoordinatorServiceProto.NumberListRequest;
import coordinatorservice.CoordinatorServiceProto.StatusRequest;
import coordinatorservice.CoordinatorServiceProto.StatusResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

// Client application that connects to the ComputationCoordinator service.
// Provides methods to submit computation tasks and retrieve results.
// Includes a command-line interface for user interaction.
public class ComputationCoordinatorClient {
    private static final Logger logger = Logger.getLogger(ComputeCoordinatorClient.class.getName());

    private ManagedChannel channel;
    private ComputationCoordinatorStub asyncStub = null;

    // Constructs a client with specified server coordinates.
    // @param host The hostname or IP address of the computation server
    // @param port The port number the server is listening on
    public void ComputationClient(String host, int port) {
        // Initialize gRPC channel with plain text (no encryption)
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.asyncStub = new ComputationCoordinatorGrpc.ComputationCoordinatorStub(channel, null);
    }

    // Shuts down the gRPC channel, terminating all ongoing RPC calls.
    // @throws InterruptedException If the shutdown is interrupted
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    // Submits a list of numbers for computation processing.
    // The method is asynchronous and will return immediately.
    // Results are handled by callbacks.
    // @param numbers The list of numbers to process
    // @param outputFile Path to save results (optional)
    // @param delimiter Character to separate values in output file
    public void submitNumberList(List<Double> numbers, String outputFile, String delimiter) {
        logger.info("Submitting number list...");

        // Build the request with all parameters
        NumberListRequest request = NumberListRequest.newBuilder()
                .addAllNumbers(numbers)
                .setOutputFile(outputFile)
                .setDelimiter(delimiter)
                .build();

        try {
            // Make an asynchronous call with callbacks
            asyncStub.submitNumberList(request, new io.grpc.stub.StreamObserver<ComputationResponse>() {
                // Called when the server returns a response.
                // Initiates status polling if computation started successfully.
                @Override
                public void onNext(ComputationResponse response) {
                    logger.info("Computation started with job ID: " + response.getJobId());

                    if (response.getSuccess()) {
                        // Poll for status
                        pollJobStatus(response.getJobId());
                    } else {
                        logger.warning("Failed to start computation: " + response.getMessage());
                    }
                }

                // Called when the RPC fails.
                @Override
                public void onError(Throwable t) {
                    logger.log(Level.WARNING, "Error submitting computation", t);
                }

                // Called when the server has completed sending responses.
                @Override
                public void onCompleted() {
                    // Nothing to do
                }
            });
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
    }

    // Submits a file containing numbers for computation processing.
    // The method is asynchronous and will return immediately.
    // Results are handled by callbacks.
    // @param filePath Path to the input file containing numbers
    // @param outputFile Path to save results (optional)
    // @param delimiter Character to separate values in output file
    public void submitFile(String filePath, String outputFile, String delimiter) {
        logger.info("Submitting file for computation: " + filePath);

        FileRequest request = FileRequest.newBuilder()
                .setFilePath(filePath)
                .setOutputFile(outputFile)
                .setDelimiter(delimiter)
                .build();

        try {
            asyncStub.submitFile(request, new io.grpc.stub.StreamObserver<ComputationResponse>() {
                @Override
                public void onNext(ComputationResponse response) {
                    logger.info("Computation started with job ID: " + response.getJobId());

                    if (response.getSuccess()) {
                        // Poll for status
                        pollJobStatus(response.getJobId());
                    } else {
                        logger.warning("Failed to start computation: " + response.getMessage());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    logger.log(Level.WARNING, "Error submitting computation", t);
                }

                @Override
                public void onCompleted() {
                    // Nothing to do
                }
            });
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
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

                    // Make asynchronous call to check status
                    asyncStub.getStatus(request, new io.grpc.stub.StreamObserver<StatusResponse>() {
                        // Process status updates from the server.
                        // Displays results when computation is completed.
                        @Override
                        public void onNext(StatusResponse response) {
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
                                    logger.warning("Computation failed: " + response.getMessage());
                                }

                                synchronized (Thread.currentThread()) {
                                    Thread.currentThread().notifyAll();
                                }
                            } else {
                                logger.info("Job still in progress...");
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            logger.log(Level.WARNING, "Error checking job status", t);
                            synchronized (Thread.currentThread()) {
                                Thread.currentThread().notifyAll();
                            }
                        }

                        @Override
                        public void onCompleted() {
                            // Nothing to do
                        }
                    });

                    synchronized (Thread.currentThread()) {
                        Thread.currentThread().wait(500);
                    }

                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Polling interrupted", e);
                    break;
                }
            }
        }).start();
    }

    // Main method to run the client as a standalone application.
    // Provides an interactive command-line interface.
    public static <ComputationClient> void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 50051;

        ComputationCoordinatorClient client = new ComputationCoordinatorClient();
        client.ComputationClient(host, port);

        try {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\n=== Computation Client ===");
                System.out.println("1. Submit number list");
                System.out.println("2. Submit file");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter numbers separated by spaces: ");
                        String numbersStr = scanner.nextLine();
                        List<Double> numbers = new ArrayList<>();

                        for (String numStr : numbersStr.split("\\s+")) {
                            if (!numStr.isEmpty()) {
                                numbers.add(Double.parseDouble(numStr));
                            }
                        }

                        System.out.print("Enter output file path (or leave empty): ");
                        String outputFile = scanner.nextLine();

                        System.out.print("Enter delimiter for output (default is comma): ");
                        String delimiter = scanner.nextLine();

                        client.submitNumberList(numbers, outputFile, delimiter);
                        break;

                    case 2:
                        System.out.print("Enter input file path: ");
                        String inputFile = scanner.nextLine();

                        System.out.print("Enter output file path: ");
                        outputFile = scanner.nextLine();

                        System.out.print("Enter delimiter for output (default is comma): ");
                        delimiter = scanner.nextLine();

                        client.submitFile(inputFile, outputFile, delimiter);
                        break;

                    case 3:
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option!");
                }
            }
        } finally {
            client.shutdown();
        }
    }
}
