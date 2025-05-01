package api;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.protobuf.services.ProtoReflectionService;

public class ComputationCoordinatorServer {
    private Server server;
    private final int port;
    
    public ComputationCoordinatorServer(int port) {
        this.port = port;
    }

    private void start() throws IOException {
        // Create the gRPC server
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
            .addService(new ComputationCoordinatorServiceImpl())
            .addService(ProtoReflectionService.newInstance()) // For reflection/debugging
            .build();
        
        // Start the server
        server.start();
        System.out.println("Server started, listening on port " + port);
        
        // Add shutdown hook to cleanly shut down server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server due to JVM shutdown");
            try {
                ComputationCoordinatorServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        // Parse port from command line arguments
        int port = 50052;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Usage: ComputationCoordinatorServer [port]");
                System.exit(1);
            }
        }
        
        // Create and start the server
        ComputationCoordinatorServer server = new ComputationCoordinatorServer(port);
        server.start();
        server.blockUntilShutdown();
    }
}
