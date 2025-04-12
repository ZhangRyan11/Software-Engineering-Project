package api;

import com.datastore.grpc.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Server implementation for the DataStorage service.
 * This server provides persistent storage for computation data and results.
 * It also handles writing results to output files when requested.
 */
public class DataStoreServer {
    private static final Logger logger = Logger.getLogger(DataStoreServer.class.getName());
    private Server server;
    private final int port;
    
    /**
     * In-memory storage for computation input data.
     * Thread-safe map where keys are job IDs and values are lists of numbers.
     */
    private static Map<String, List<Double>> dataStore = new ConcurrentHashMap<>();
    
    /**
     * In-memory storage for computation results.
     * Thread-safe map where keys are job IDs and values are lists of result values.
     */
    private static Map<String, List<Double>> resultsStore = new ConcurrentHashMap<>();
    
    /**
     * Constructs a data store server with the specified port.
     * 
     * @param port The port to listen on for client connections
     */
    public DataStoreServer(int port) {
        this.port = port;
    }
    
    /**
     * Starts the gRPC server and begins listening for client connections.
     * 
     * @throws IOException If the server cannot be started
     */
    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new DataStorageImpl())
                .build()
                .start();
        
        logger.info("Data Store Server started, listening on port " + port);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Shutting down gRPC server");
                try {
                    DataStoreServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                logger.info("Server shut down");
            }
        });
    }
    
    /**
     * Stops the server, allowing current requests to complete within timeout.
     * 
     * @throws InterruptedException If the shutdown is interrupted
     */
    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }
    
    /**
     * Blocks the calling thread until the server is shut down.
     * 
     * @throws InterruptedException If the thread is interrupted while waiting
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
    
    /**
     * Implementation of the DataStorage service.
     * Handles storage and retrieval of computation data and results.
     */
    private class DataStorageImpl extends DataStorageGrpc.DataStorageImplBase {
        /**
         * Stores input data for a computation job.
         * 
         * @param request Contains job ID and data to store
         * @param responseObserver Used to send the response back to the client
         */
        @Override
        public void storeData(StoreRequest request, StreamObserver<StoreResponse> responseObserver) {
            // Extract job ID and data from request
            String jobId = request.getJobId();
            List<Double> data = new ArrayList<>(request.getDataList());
            
            // Store data in memory
            dataStore.put(jobId, data);
            
            // Create success response
            StoreResponse response = StoreResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Data stored successfully")
                    .build();
            
            // Send response
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        
        /**
         * Retrieves input data for a computation job.
         * 
         * @param request Contains job ID to retrieve data for
         * @param responseObserver Used to send the response back to the client
         */
        @Override
        public void retrieveData(RetrieveRequest request, StreamObserver<RetrieveResponse> responseObserver) {
            String jobId = request.getJobId();
            List<Double> data = dataStore.get(jobId);
            
            RetrieveResponse.Builder responseBuilder = RetrieveResponse.newBuilder();
            
            if (data != null) {
                responseBuilder.setSuccess(true)
                        .addAllData(data)
                        .setMessage("Data retrieved successfully");
            } else {
                responseBuilder.setSuccess(false)
                        .setMessage("Job ID not found");
            }
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }
        
        /**
         * Stores computation results and writes to an output file if specified.
         * 
         * @param request Contains job ID, results and output preferences
         * @param responseObserver Used to send the response back to the client
         */
        @Override
        public void storeResults(ResultsRequest request, StreamObserver<ResultsResponse> responseObserver) {
            // Extract job ID and results from request
            String jobId = request.getJobId();
            List<Double> results = new ArrayList<>(request.getResultsList());
            String outputFile = request.getOutputFile();
            String delimiter = request.getDelimiter().isEmpty() ? "," : request.getDelimiter();
            
            // Store results in memory
            resultsStore.put(jobId, results);
            
            // Initialize response variables
            boolean success = true;
            String message = "Results stored and written to file successfully";
            
            // Write results to output file if specified
            if (!outputFile.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    // Write header row with column names
                    writer.write("Sum" + delimiter + "Average" + delimiter + "Min" + delimiter + "Max");
                    writer.newLine();
                    
                    // Write results row
                    if (results.size() >= 4) {
                        // All expected results are available
                        writer.write(results.get(0) + delimiter + 
                                     results.get(1) + delimiter + 
                                     results.get(2) + delimiter + 
                                     results.get(3));
                    } else {
                        // Write whatever results are available
                        for (int i = 0; i < results.size(); i++) {
                            if (i > 0) writer.write(delimiter);
                            writer.write(String.valueOf(results.get(i)));
                        }
                    }
                } catch (IOException e) {
                    // Handle file writing errors
                    success = false;
                    message = "Error writing to output file: " + e.getMessage();
                    logger.warning(message);
                }
            }
            
            // Create response with success status and message
            ResultsResponse response = ResultsResponse.newBuilder()
                    .setSuccess(success)
                    .setMessage(message)
                    .build();
            
            // Send response
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        
        /**
         * Retrieves computation results for a job.
         * 
         * @param request Contains job ID to retrieve results for
         * @param responseObserver Used to send the response back to the client
         */
        @Override
        public void retrieveResults(RetrieveResultsRequest request, StreamObserver<RetrieveResultsResponse> responseObserver) {
            String jobId = request.getJobId();
            List<Double> results = resultsStore.get(jobId);
            
            RetrieveResultsResponse.Builder responseBuilder = RetrieveResultsResponse.newBuilder();
            
            if (results != null) {
                responseBuilder.setSuccess(true)
                        .addAllResults(results)
                        .setMessage("Results retrieved successfully");
            } else {
                responseBuilder.setSuccess(false)
                        .setMessage("Job ID not found or computation not completed");
            }
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }
    }
    
    /**
     * Main method to run the server as a standalone application.
     */
    public static void main(String[] args) throws Exception {
        int port = 50052;
        DataStoreServer server = new DataStoreServer(port);
        server.start();
        server.blockUntilShutdown();
    }
}
