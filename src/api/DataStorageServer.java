package api;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.protobuf.services.ProtoReflectionService;

public class DataStorageServer { // Boilerplate: Changed name of class
    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
    	//Port is one off from Coordinator port 
        int port = 50056; // Boilerplate: Set port number (only one server per port)
        DataStoreImpl ds = new DataStoreImpl();
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new DataStorageImpl(ds)) // Boilerplate: Changed name of class
                .addService(ProtoReflectionService.newInstance())
                .build()
                .start();
        System.out.println("Server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    if (server != null) {
                        server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
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
        DataStorageServer server = new DataStorageServer(); // Boilerplate: Changed name of class
        server.start();
        server.blockUntilShutdown();
    }
}