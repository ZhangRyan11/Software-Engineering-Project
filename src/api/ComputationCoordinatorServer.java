package api;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

// Server implementation for the ComputationCoordinator service.
// This server acts as a coordinator between clients and the data storage service.
// It processes computation requests, manages job status, and returns results to clients.
public class ComputationCoordinatorServer {
    private static final Logger logger = Logger.getLogger(ComputationCoordinatorServer.class.getName());
    private Server server;
    private final int port;
    private final String dataStoreAddress;
    private final int dataStorePort;

    // Thread-safe map to store the status of all submitted jobs.
    // Keys are job IDs and values are JobStatus objects.
    private static ConcurrentHashMap<String, JobStatus> jobStatuses = new ConcurrentHashMap<>();

    // Inner class to represent the status of a computation job.
    // Used to track job progress and store results.
    static class JobStatus {
        boolean completed;     // Whether the job has completed (success or failure)
        boolean success;       // Whether the job completed successfully
        String message;        // Status message or error details
        List<Double> results;  // Computation results if successful

        JobStatus() {
            this.completed = false;
            this.success = false;
            this.message = "";
            this.results = new ArrayList<>();
        }
    }

    // Constructs a server with specified port and data store coordinates.
    // @param port The port to listen on for client connections
    // @param dataStoreAddress The hostname or IP address of the data store server
    // @param dataStorePort The port of the data store server
    public ComputationCoordinatorServer(int port, String dataStoreAddress, int dataStorePort) {
        this.port = port;
        this.dataStoreAddress = dataStoreAddress;
        this.dataStorePort = dataStorePort;
    }

    // Starts the gRPC server and begins listening for client connections.
    // @throws IOException If the server cannot be started
    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new ComputationCoordinatorImpl())
                .build()
                .start();

        logger.info("Compute Server started, listening on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Shutting down gRPC server");
                try {
                    ComputationCoordinatorServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                logger.info("Server shut down");
            }
        });
    }

    // Stops the server, allowing current requests to complete within timeout.
    // @throws InterruptedException If the shutdown is interrupted
    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    // Blocks the calling thread until the server is shut down.
    // @throws InterruptedException If the thread is interrupted while waiting
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    // Implementation of the ComputationCoordinator service.
    // Handles client requests for computation tasks.
    private class ComputationCoordinatorImpl extends ComputationCoordinatorGrpc.ComputationCoordinatorImplBase {
        // Processes a list of numbers submitted by a client.
        // Creates a new job, stores the data in the data store service,
        // performs computation, and updates job status.
        // @param request The request containing numbers and output preferences
        // @param responseObserver Used to send the response back to the client
        @Override
        public void submitNumberList(NumberListRequest request, StreamObserver<ComputationResponse> responseObserver) {
            // Generate a unique job ID for this computation request
            String jobId = UUID.randomUUID().toString();

            // Initialize job status
            JobStatus status = new JobStatus();
            jobStatuses.put(jobId, status);

            // Process the computation in a new thread to avoid blocking the RPC thread
            new Thread(() -> {
                try {
                    // Create a gRPC channel to the data store service
                    ManagedChannel channel = ManagedChannelBuilder.forAddress(dataStoreAddress, dataStorePort)
                            .usePlaintext()
                            .build();
                    DataStorageGrpc.DataStorageStub dataStoreStub = new DataStorageGrpc.DataStorageStub(channel);

                    // Build a request to store the input data
                    StoreRequest storeRequest = StoreRequest.newBuilder()
                            .setJobId(jobId)
                            .addAllData(request.getNumbersList())
                            .build();

                    // Send the data to the data store service
                    dataStoreStub.storeData(storeRequest, new StreamObserver<StoreResponse>() {
                        @Override
                        public void onNext(StoreResponse response) {
                            if (response.getSuccess()) {
                                // Data was stored successfully, now perform computation
                                List<Double> results = performComputation(request.getNumbersList());

                                // Store the computation results in the data store
                                ResultsRequest resultsRequest = ResultsRequest.newBuilder()
                                        .setJobId(jobId)
                                        .addAllResults(results)
                                        .setOutputFile(request.getOutputFile())
                                        .setDelimiter(request.getDelimiter())
                                        .build();

                                // Send the results to the data store service
                                dataStoreStub.storeResults(resultsRequest, new StreamObserver<ResultsResponse>() {
                                    @Override
                                    public void onNext(ResultsResponse response) {
                                        JobStatus status = jobStatuses.get(jobId);
                                        status.completed = true;
                                        status.success = response.getSuccess();
                                        status.message = response.getMessage();
                                        status.results = results;
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        JobStatus status = jobStatuses.get(jobId);
                                        status.completed = true;
                                        status.success = false;
                                        status.message = "Error storing results: " + t.getMessage();
                                    }

                                    @Override
                                    public void onCompleted() {
                                        // Nothing to do
                                    }
                                });
                            } else {
                                // Data store reported an error
                                JobStatus status = jobStatuses.get(jobId);
                                status.completed = true;
                                status.success = false;
                                status.message = "Error storing data: " + response.getMessage();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            JobStatus status = jobStatuses.get(jobId);
                            status.completed = true;
                            status.success = false;
                            status.message = "Error communicating with data store: " + t.getMessage();
                        }

                        @Override
                        public void onCompleted() {
                            // Nothing to do
                        }
                    });

                    // Clean up the channel when done
                    channel.shutdown();
                } catch (Exception e) {
                    // Handle any unexpected errors
                    JobStatus status = jobStatuses.get(jobId);
                    status.completed = true;
                    status.success = false;
                    status.message = "Internal error: " + e.getMessage();
                }
            }).start();

            // Return the job ID to the client immediately
            ComputationResponse response = ComputationResponse.newBuilder()
                    .setJobId(jobId)
                    .setSuccess(true)
                    .setMessage("Computation started")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        // Processes a file of numbers submitted by a client.
        // Reads the file, stores data in the data store service,
        // performs computation, and updates job status.
        // @param request The request containing file path and output preferences
        // @param responseObserver Used to send the response back to the client
        @Override
        public void submitFile(FileRequest request, StreamObserver<ComputationResponse> responseObserver) {
            String jobId = UUID.randomUUID().toString();

            // Create job status
            JobStatus status = new JobStatus();
            jobStatuses.put(jobId, status);

            // Start computation in a new thread
            new Thread(() -> {
                try {
                    // Read file and convert to numbers
                    List<Double> numbers = readNumbersFromFile(request.getFilePath());

                    // Connect to data storage service
                    ManagedChannel channel = ManagedChannelBuilder.forAddress(dataStoreAddress, dataStorePort)
                            .usePlaintext()
                            .build();
                    DataStorageGrpc.DataStorageStub dataStoreStub = new DataStorageGrpc.DataStorageStub(channel);

                    // Store the data
                    StoreRequest storeRequest = StoreRequest.newBuilder()
                            .setJobId(jobId)
                            .addAllData(numbers)
                            .build();

                    dataStoreStub.storeData(storeRequest, new StreamObserver<StoreResponse>() {
                        @Override
                        public void onNext(StoreResponse response) {
                            if (response.getSuccess()) {
                                // Perform computation (placeholder for actual computation logic)
                                List<Double> results = performComputation(numbers);

                                // Store results
                                ResultsRequest resultsRequest = ResultsRequest.newBuilder()
                                        .setJobId(jobId)
                                        .addAllResults(results)
                                        .setOutputFile(request.getOutputFile())
                                        .setDelimiter(request.getDelimiter())
                                        .build();

                                dataStoreStub.storeResults(resultsRequest, new StreamObserver<ResultsResponse>() {
                                    @Override
                                    public void onNext(ResultsResponse response) {
                                        JobStatus status = jobStatuses.get(jobId);
                                        status.completed = true;
                                        status.success = response.getSuccess();
                                        status.message = response.getMessage();
                                        status.results = results;
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        JobStatus status = jobStatuses.get(jobId);
                                        status.completed = true;
                                        status.success = false;
                                        status.message = "Error storing results: " + t.getMessage();
                                    }

                                    @Override
                                    public void onCompleted() {
                                        // Nothing to do
                                    }
                                });
                            } else {
                                JobStatus status = jobStatuses.get(jobId);
                                status.completed = true;
                                status.success = false;
                                status.message = "Error storing data: " + response.getMessage();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            JobStatus status = jobStatuses.get(jobId);
                            status.completed = true;
                            status.success = false;
                            status.message = "Error communicating with data store: " + t.getMessage();
                        }

                        @Override
                        public void onCompleted() {
                            // Nothing to do
                        }
                    });

                    channel.shutdown();
                } catch (Exception e) {
                    JobStatus status = jobStatuses.get(jobId);
                    status.completed = true;
                    status.success = false;
                    status.message = "Internal error: " + e.getMessage();
                }
            }).start();

            // Return job ID to client
            ComputationResponse response = ComputationResponse.newBuilder()
                    .setJobId(jobId)
                    .setSuccess(true)
                    .setMessage("Computation started")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        // Returns the current status of a computation job.
        // @param request The request containing the job ID to check
        // @param responseObserver Used to send the response back to the client
        @Override
        public void getStatus(StatusRequest request, StreamObserver<StatusResponse> responseObserver) {
            String jobId = request.getJobId();
            JobStatus status = jobStatuses.get(jobId);

            if (status == null) {
                StatusResponse response = StatusResponse.newBuilder()
                        .setCompleted(true)
                        .setSuccess(false)
                        .setMessage("Job ID not found")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            StatusResponse response = StatusResponse.newBuilder()
                    .setCompleted(status.completed)
                    .setSuccess(status.success)
                    .setMessage(status.message)
                    .addAllResults(status.results)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        // Reads numbers from a file and returns them as a list of doubles.
        // @param filePath Path to the file containing numbers
        // @return List of numbers read from the file
        // @throws IOException If there's an error reading the file
        private List<Double> readNumbersFromFile(String filePath) throws IOException {
            List<Double> numbers = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.trim().split("\\s+");
                    for (String part : parts) {
                        if (!part.isEmpty()) {
                            numbers.add(Double.parseDouble(part));
                        }
                    }
                }
            }
            return numbers;
        }

        // Performs computation on a list of numbers.
        // Currently calculates sum, average, min, and max.
        // @param numbers List of numbers to process
        // @return List containing results in order: sum, average, min, max
        private List<Double> performComputation(List<Double> numbers) {
            // Placeholder for actual computation logic
            // For demonstration, we'll just calculate sum, average, min, max
            List<Double> results = new ArrayList<>();

            if (numbers.isEmpty()) {
                return results;
            }

            double sum = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            for (Double num : numbers) {
                sum += num;
                min = Math.min(min, num);
                max = Math.max(max, num);
            }

            double avg = sum / numbers.size();

            results.add(sum);
            results.add(avg);
            results.add(min);
            results.add(max);

            return results;
        }
    }

    // Main method to run the server as a standalone application.
    public static void main(String[] args) throws Exception {
        int port = 50051;
        String dataStoreAddress = "localhost";
        int dataStorePort = 50052;

        ComputationCoordinatorServer server = new ComputationCoordinatorServer(port, dataStoreAddress, dataStorePort);
        server.start();
        server.blockUntilShutdown();
    }
}
