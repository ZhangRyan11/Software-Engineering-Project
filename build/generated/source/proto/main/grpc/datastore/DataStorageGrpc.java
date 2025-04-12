package datastore;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.1)",
    comments = "Source: datastore_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DataStorageGrpc {

  private DataStorageGrpc() {}

  public static final java.lang.String SERVICE_NAME = "datastore.DataStorage";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<datastore.DatastoreService.ReadRequest,
      datastore.DatastoreService.ReadResponse> getReadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "read",
      requestType = datastore.DatastoreService.ReadRequest.class,
      responseType = datastore.DatastoreService.ReadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<datastore.DatastoreService.ReadRequest,
      datastore.DatastoreService.ReadResponse> getReadMethod() {
    io.grpc.MethodDescriptor<datastore.DatastoreService.ReadRequest, datastore.DatastoreService.ReadResponse> getReadMethod;
    if ((getReadMethod = DataStorageGrpc.getReadMethod) == null) {
      synchronized (DataStorageGrpc.class) {
        if ((getReadMethod = DataStorageGrpc.getReadMethod) == null) {
          DataStorageGrpc.getReadMethod = getReadMethod =
              io.grpc.MethodDescriptor.<datastore.DatastoreService.ReadRequest, datastore.DatastoreService.ReadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "read"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  datastore.DatastoreService.ReadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  datastore.DatastoreService.ReadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataStorageMethodDescriptorSupplier("read"))
              .build();
        }
      }
    }
    return getReadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<datastore.DatastoreService.WriteRequest,
      datastore.DatastoreService.WriteResponse> getWriteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "write",
      requestType = datastore.DatastoreService.WriteRequest.class,
      responseType = datastore.DatastoreService.WriteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<datastore.DatastoreService.WriteRequest,
      datastore.DatastoreService.WriteResponse> getWriteMethod() {
    io.grpc.MethodDescriptor<datastore.DatastoreService.WriteRequest, datastore.DatastoreService.WriteResponse> getWriteMethod;
    if ((getWriteMethod = DataStorageGrpc.getWriteMethod) == null) {
      synchronized (DataStorageGrpc.class) {
        if ((getWriteMethod = DataStorageGrpc.getWriteMethod) == null) {
          DataStorageGrpc.getWriteMethod = getWriteMethod =
              io.grpc.MethodDescriptor.<datastore.DatastoreService.WriteRequest, datastore.DatastoreService.WriteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "write"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  datastore.DatastoreService.WriteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  datastore.DatastoreService.WriteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DataStorageMethodDescriptorSupplier("write"))
              .build();
        }
      }
    }
    return getWriteMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DataStorageStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataStorageStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataStorageStub>() {
        @java.lang.Override
        public DataStorageStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataStorageStub(channel, callOptions);
        }
      };
    return DataStorageStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DataStorageBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataStorageBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataStorageBlockingStub>() {
        @java.lang.Override
        public DataStorageBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataStorageBlockingStub(channel, callOptions);
        }
      };
    return DataStorageBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DataStorageFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataStorageFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataStorageFutureStub>() {
        @java.lang.Override
        public DataStorageFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataStorageFutureStub(channel, callOptions);
        }
      };
    return DataStorageFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void read(datastore.DatastoreService.ReadRequest request,
        io.grpc.stub.StreamObserver<datastore.DatastoreService.ReadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReadMethod(), responseObserver);
    }

    /**
     */
    default void write(datastore.DatastoreService.WriteRequest request,
        io.grpc.stub.StreamObserver<datastore.DatastoreService.WriteResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWriteMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DataStorage.
   */
  public static abstract class DataStorageImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DataStorageGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DataStorage.
   */
  public static final class DataStorageStub
      extends io.grpc.stub.AbstractAsyncStub<DataStorageStub> {
    private DataStorageStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataStorageStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataStorageStub(channel, callOptions);
    }

    /**
     */
    public void read(datastore.DatastoreService.ReadRequest request,
        io.grpc.stub.StreamObserver<datastore.DatastoreService.ReadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void write(datastore.DatastoreService.WriteRequest request,
        io.grpc.stub.StreamObserver<datastore.DatastoreService.WriteResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DataStorage.
   */
  public static final class DataStorageBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DataStorageBlockingStub> {
    private DataStorageBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataStorageBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataStorageBlockingStub(channel, callOptions);
    }

    /**
     */
    public datastore.DatastoreService.ReadResponse read(datastore.DatastoreService.ReadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReadMethod(), getCallOptions(), request);
    }

    /**
     */
    public datastore.DatastoreService.WriteResponse write(datastore.DatastoreService.WriteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getWriteMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DataStorage.
   */
  public static final class DataStorageFutureStub
      extends io.grpc.stub.AbstractFutureStub<DataStorageFutureStub> {
    private DataStorageFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataStorageFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataStorageFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<datastore.DatastoreService.ReadResponse> read(
        datastore.DatastoreService.ReadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReadMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<datastore.DatastoreService.WriteResponse> write(
        datastore.DatastoreService.WriteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_READ = 0;
  private static final int METHODID_WRITE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_READ:
          serviceImpl.read((datastore.DatastoreService.ReadRequest) request,
              (io.grpc.stub.StreamObserver<datastore.DatastoreService.ReadResponse>) responseObserver);
          break;
        case METHODID_WRITE:
          serviceImpl.write((datastore.DatastoreService.WriteRequest) request,
              (io.grpc.stub.StreamObserver<datastore.DatastoreService.WriteResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getReadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              datastore.DatastoreService.ReadRequest,
              datastore.DatastoreService.ReadResponse>(
                service, METHODID_READ)))
        .addMethod(
          getWriteMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              datastore.DatastoreService.WriteRequest,
              datastore.DatastoreService.WriteResponse>(
                service, METHODID_WRITE)))
        .build();
  }

  private static abstract class DataStorageBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DataStorageBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return datastore.DatastoreService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DataStorage");
    }
  }

  private static final class DataStorageFileDescriptorSupplier
      extends DataStorageBaseDescriptorSupplier {
    DataStorageFileDescriptorSupplier() {}
  }

  private static final class DataStorageMethodDescriptorSupplier
      extends DataStorageBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DataStorageMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DataStorageGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DataStorageFileDescriptorSupplier())
              .addMethod(getReadMethod())
              .addMethod(getWriteMethod())
              .build();
        }
      }
    }
    return result;
  }
}