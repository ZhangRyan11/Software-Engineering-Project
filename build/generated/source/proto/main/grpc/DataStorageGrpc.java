package com.datastore.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * This class contains the gRPC service definition for the DataStorage service.
 * It's auto-generated from the datastoreservice.proto file.
 * The class provides:
 * 1. Method descriptors for all service RPC methods
 * 2. Base implementation class that servers can extend
 * 3. Client stubs for making RPC calls
 */
public class DataStorageGrpc {
    // Private constructor prevents instantiation
    private DataStorageGrpc() {}

    // Service name as defined in the proto file
    public static final String SERVICE_NAME = "DataStorage";

    /**
     * Static method descriptors for the service methods.
     * These descriptors contain metadata about each RPC method including:
     * - Method type (unary, streaming, etc.)
     * - Full method name
     * - Marshalling configuration
     */
    public static final io.grpc.MethodDescriptor<StoreRequest, StoreResponse> METHOD_STORE_DATA = 
        createMethodDescriptor("StoreData", io.grpc.MethodDescriptor.MethodType.UNARY);
    
    public static final io.grpc.MethodDescriptor<RetrieveRequest, RetrieveResponse> METHOD_RETRIEVE_DATA = 
        createMethodDescriptor("RetrieveData", io.grpc.MethodDescriptor.MethodType.UNARY);
    
    public static final io.grpc.MethodDescriptor<ResultsRequest, ResultsResponse> METHOD_STORE_RESULTS = 
        createMethodDescriptor("StoreResults", io.grpc.MethodDescriptor.MethodType.UNARY);
    
    public static final io.grpc.MethodDescriptor<RetrieveResultsRequest, RetrieveResultsResponse> METHOD_RETRIEVE_RESULTS = 
        createMethodDescriptor("RetrieveResults", io.grpc.MethodDescriptor.MethodType.UNARY);

    /**
     * Helper method to create method descriptors with consistent configuration.
     * @param methodName The name of the method from the proto definition
     * @param methodType The type of RPC call (UNARY, SERVER_STREAMING, etc.)
     * @return A configured MethodDescriptor for the specified method
     */
    private static <ReqT, RespT> io.grpc.MethodDescriptor<ReqT, RespT> createMethodDescriptor(
            String methodName, io.grpc.MethodDescriptor.MethodType methodType) {
        return io.grpc.MethodDescriptor.<ReqT, RespT>newBuilder()
            .setType(methodType)
            .setFullMethodName(generateFullMethodName(SERVICE_NAME, methodName))
            .setSampledToLocalTracing(true)
            .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                (com.google.protobuf.Message) null)) // This will be replaced by Protobuf implementation
            .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                (com.google.protobuf.Message) null)) // This will be replaced by Protobuf implementation
            .setSchemaDescriptor(null)
            .build();
    }

    /**
     * Abstract base class for service implementations.
     * Server implementations will extend this class and override the methods.
     * This class provides default no-op implementations that return "unimplemented" errors.
     */
    public static abstract class DataStorageImplBase implements io.grpc.BindableService {
        /**
         * Stores input data for a computation job.
         * @param request Contains job ID and data to store
         * @param responseObserver Used to send the response back to the client
         */
        public void storeData(StoreRequest request, io.grpc.stub.StreamObserver<StoreResponse> responseObserver) {
            asyncUnimplementedUnaryCall(METHOD_STORE_DATA, responseObserver);
        }

        /**
         * Retrieves stored data for a computation job.
         * @param request Contains job ID for which data is to be retrieved
         * @param responseObserver Used to send the response back to the client
         */
        public void retrieveData(RetrieveRequest request, io.grpc.stub.StreamObserver<RetrieveResponse> responseObserver) {
            asyncUnimplementedUnaryCall(METHOD_RETRIEVE_DATA, responseObserver);
        }

        /**
         * Stores results of a computation job.
         * @param request Contains job ID and results to store
         * @param responseObserver Used to send the response back to the client
         */
        public void storeResults(ResultsRequest request, io.grpc.stub.StreamObserver<ResultsResponse> responseObserver) {
            asyncUnimplementedUnaryCall(METHOD_STORE_RESULTS, responseObserver);
        }

        /**
         * Retrieves stored results of a computation job.
         * @param request Contains job ID for which results are to be retrieved
         * @param responseObserver Used to send the response back to the client
         */
        public void retrieveResults(RetrieveResultsRequest request, io.grpc.stub.StreamObserver<RetrieveResultsResponse> responseObserver) {
            asyncUnimplementedUnaryCall(METHOD_RETRIEVE_RESULTS, responseObserver);
        }

        /**
         * Binds all service methods to their implementations.
         * Creates a service definition that can be registered with a gRPC server.
         */
        @Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(METHOD_STORE_DATA, asyncUnaryCall(
                    (request, observer) -> storeData((StoreRequest) request, observer)))
                .addMethod(METHOD_RETRIEVE_DATA, asyncUnaryCall(
                    (request, observer) -> retrieveData((RetrieveRequest) request, observer)))
                .addMethod(METHOD_STORE_RESULTS, asyncUnaryCall(
                    (request, observer) -> storeResults((ResultsRequest) request, observer)))
                .addMethod(METHOD_RETRIEVE_RESULTS, asyncUnaryCall(
                    (request, observer) -> retrieveResults((RetrieveResultsRequest) request, observer)))
                .build();
        }
    }

    /**
     * Client-side stub for making RPC calls to the DataStorage service.
     * Contains methods that mirror the service definition to invoke remote procedures.
     */
    public static final class DataStorageStub extends io.grpc.stub.AbstractStub<DataStorageStub> {
        private DataStorageStub(io.grpc.Channel channel) {
            super(channel);
        }

        private DataStorageStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected DataStorageStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new DataStorageStub(channel, callOptions);
        }

        /**
         * Stores input data for a computation job.
         * @param request The request containing job ID and data
         * @param responseObserver Observer to receive the async response
         */
        public void storeData(StoreRequest request, io.grpc.stub.StreamObserver<StoreResponse> responseObserver) {
            asyncUnaryCall(getChannel().newCall(METHOD_STORE_DATA, getCallOptions()), request, responseObserver);
        }

        /**
         * Retrieves stored data for a computation job.
         * @param request The request containing job ID for which data is to be retrieved
         * @param responseObserver Observer to receive the async response
         */
        public void retrieveData(RetrieveRequest request, io.grpc.stub.StreamObserver<RetrieveResponse> responseObserver) {
            asyncUnaryCall(getChannel().newCall(METHOD_RETRIEVE_DATA, getCallOptions()), request, responseObserver);
        }

        /**
         * Stores results of a computation job.
         * @param request The request containing job ID and results
         * @param responseObserver Observer to receive the async response
         */
        public void storeResults(ResultsRequest request, io.grpc.stub.StreamObserver<ResultsResponse> responseObserver) {
            asyncUnaryCall(getChannel().newCall(METHOD_STORE_RESULTS, getCallOptions()), request, responseObserver);
        }

        /**
         * Retrieves stored results of a computation job.
         * @param request The request containing job ID for which results are to be retrieved
         * @param responseObserver Observer to receive the async response
         */
        public void retrieveResults(RetrieveResultsRequest request, io.grpc.stub.StreamObserver<RetrieveResultsResponse> responseObserver) {
            asyncUnaryCall(getChannel().newCall(METHOD_RETRIEVE_RESULTS, getCallOptions()), request, responseObserver);
        }
    }

    /**
     * Service descriptor that contains metadata about the service.
     * Used during service registration with the gRPC runtime.
     */
    private static io.grpc.ServiceDescriptor serviceDescriptor;
    
    /**
     * Retrieves the service descriptor, creating it if necessary.
     * @return The service descriptor for this service
     */
    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        if (serviceDescriptor == null) {
            serviceDescriptor = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                .addMethod(METHOD_STORE_DATA)
                .addMethod(METHOD_RETRIEVE_DATA)
                .addMethod(METHOD_STORE_RESULTS)
                .addMethod(METHOD_RETRIEVE_RESULTS)
                .build();
        }
        return serviceDescriptor;
    }
}
