package service;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.1)",
    comments = "Source: coordinator_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ComputationCoordinatorGrpc {

  private ComputationCoordinatorGrpc() {}

  public static final java.lang.String SERVICE_NAME = "service.ComputationCoordinator";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<service.CoordinatorService.ComputeRequest,
      service.CoordinatorService.ComputeResult> getComputeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "compute",
      requestType = service.CoordinatorService.ComputeRequest.class,
      responseType = service.CoordinatorService.ComputeResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<service.CoordinatorService.ComputeRequest,
      service.CoordinatorService.ComputeResult> getComputeMethod() {
    io.grpc.MethodDescriptor<service.CoordinatorService.ComputeRequest, service.CoordinatorService.ComputeResult> getComputeMethod;
    if ((getComputeMethod = ComputationCoordinatorGrpc.getComputeMethod) == null) {
      synchronized (ComputationCoordinatorGrpc.class) {
        if ((getComputeMethod = ComputationCoordinatorGrpc.getComputeMethod) == null) {
          ComputationCoordinatorGrpc.getComputeMethod = getComputeMethod =
              io.grpc.MethodDescriptor.<service.CoordinatorService.ComputeRequest, service.CoordinatorService.ComputeResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "compute"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.CoordinatorService.ComputeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.CoordinatorService.ComputeResult.getDefaultInstance()))
              .setSchemaDescriptor(new ComputationCoordinatorMethodDescriptorSupplier("compute"))
              .build();
        }
      }
    }
    return getComputeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<service.CoordinatorService.ComputeRequest,
      service.CoordinatorService.ComputeResult> getComputeSingleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "computeSingle",
      requestType = service.CoordinatorService.ComputeRequest.class,
      responseType = service.CoordinatorService.ComputeResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<service.CoordinatorService.ComputeRequest,
      service.CoordinatorService.ComputeResult> getComputeSingleMethod() {
    io.grpc.MethodDescriptor<service.CoordinatorService.ComputeRequest, service.CoordinatorService.ComputeResult> getComputeSingleMethod;
    if ((getComputeSingleMethod = ComputationCoordinatorGrpc.getComputeSingleMethod) == null) {
      synchronized (ComputationCoordinatorGrpc.class) {
        if ((getComputeSingleMethod = ComputationCoordinatorGrpc.getComputeSingleMethod) == null) {
          ComputationCoordinatorGrpc.getComputeSingleMethod = getComputeSingleMethod =
              io.grpc.MethodDescriptor.<service.CoordinatorService.ComputeRequest, service.CoordinatorService.ComputeResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "computeSingle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.CoordinatorService.ComputeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  service.CoordinatorService.ComputeResult.getDefaultInstance()))
              .setSchemaDescriptor(new ComputationCoordinatorMethodDescriptorSupplier("computeSingle"))
              .build();
        }
      }
    }
    return getComputeSingleMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ComputationCoordinatorStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ComputationCoordinatorStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ComputationCoordinatorStub>() {
        @java.lang.Override
        public ComputationCoordinatorStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ComputationCoordinatorStub(channel, callOptions);
        }
      };
    return ComputationCoordinatorStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ComputationCoordinatorBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ComputationCoordinatorBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ComputationCoordinatorBlockingStub>() {
        @java.lang.Override
        public ComputationCoordinatorBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ComputationCoordinatorBlockingStub(channel, callOptions);
        }
      };
    return ComputationCoordinatorBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ComputationCoordinatorFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ComputationCoordinatorFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ComputationCoordinatorFutureStub>() {
        @java.lang.Override
        public ComputationCoordinatorFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ComputationCoordinatorFutureStub(channel, callOptions);
        }
      };
    return ComputationCoordinatorFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void compute(service.CoordinatorService.ComputeRequest request,
        io.grpc.stub.StreamObserver<service.CoordinatorService.ComputeResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getComputeMethod(), responseObserver);
    }

    /**
     */
    default void computeSingle(service.CoordinatorService.ComputeRequest request,
        io.grpc.stub.StreamObserver<service.CoordinatorService.ComputeResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getComputeSingleMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ComputationCoordinator.
   */
  public static abstract class ComputationCoordinatorImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ComputationCoordinatorGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ComputationCoordinator.
   */
  public static final class ComputationCoordinatorStub
      extends io.grpc.stub.AbstractAsyncStub<ComputationCoordinatorStub> {
    private ComputationCoordinatorStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ComputationCoordinatorStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ComputationCoordinatorStub(channel, callOptions);
    }

    /**
     */
    public void compute(service.CoordinatorService.ComputeRequest request,
        io.grpc.stub.StreamObserver<service.CoordinatorService.ComputeResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getComputeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void computeSingle(service.CoordinatorService.ComputeRequest request,
        io.grpc.stub.StreamObserver<service.CoordinatorService.ComputeResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getComputeSingleMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ComputationCoordinator.
   */
  public static final class ComputationCoordinatorBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ComputationCoordinatorBlockingStub> {
    private ComputationCoordinatorBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ComputationCoordinatorBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ComputationCoordinatorBlockingStub(channel, callOptions);
    }

    /**
     */
    public service.CoordinatorService.ComputeResult compute(service.CoordinatorService.ComputeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getComputeMethod(), getCallOptions(), request);
    }

    /**
     */
    public service.CoordinatorService.ComputeResult computeSingle(service.CoordinatorService.ComputeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getComputeSingleMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ComputationCoordinator.
   */
  public static final class ComputationCoordinatorFutureStub
      extends io.grpc.stub.AbstractFutureStub<ComputationCoordinatorFutureStub> {
    private ComputationCoordinatorFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ComputationCoordinatorFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ComputationCoordinatorFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<service.CoordinatorService.ComputeResult> compute(
        service.CoordinatorService.ComputeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getComputeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<service.CoordinatorService.ComputeResult> computeSingle(
        service.CoordinatorService.ComputeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getComputeSingleMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_COMPUTE = 0;
  private static final int METHODID_COMPUTE_SINGLE = 1;

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
        case METHODID_COMPUTE:
          serviceImpl.compute((service.CoordinatorService.ComputeRequest) request,
              (io.grpc.stub.StreamObserver<service.CoordinatorService.ComputeResult>) responseObserver);
          break;
        case METHODID_COMPUTE_SINGLE:
          serviceImpl.computeSingle((service.CoordinatorService.ComputeRequest) request,
              (io.grpc.stub.StreamObserver<service.CoordinatorService.ComputeResult>) responseObserver);
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
          getComputeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              service.CoordinatorService.ComputeRequest,
              service.CoordinatorService.ComputeResult>(
                service, METHODID_COMPUTE)))
        .addMethod(
          getComputeSingleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              service.CoordinatorService.ComputeRequest,
              service.CoordinatorService.ComputeResult>(
                service, METHODID_COMPUTE_SINGLE)))
        .build();
  }

  private static abstract class ComputationCoordinatorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ComputationCoordinatorBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return service.CoordinatorService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ComputationCoordinator");
    }
  }

  private static final class ComputationCoordinatorFileDescriptorSupplier
      extends ComputationCoordinatorBaseDescriptorSupplier {
    ComputationCoordinatorFileDescriptorSupplier() {}
  }

  private static final class ComputationCoordinatorMethodDescriptorSupplier
      extends ComputationCoordinatorBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ComputationCoordinatorMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ComputationCoordinatorGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ComputationCoordinatorFileDescriptorSupplier())
              .addMethod(getComputeMethod())
              .addMethod(getComputeSingleMethod())
              .build();
        }
      }
    }
    return result;
  }
}