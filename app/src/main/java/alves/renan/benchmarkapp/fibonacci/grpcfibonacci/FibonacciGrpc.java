package grpcfibonacci;

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
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.26.0)",
    comments = "Source: fibonacci.proto")
public final class FibonacciGrpc {

  private FibonacciGrpc() {}

  public static final String SERVICE_NAME = "grpcfibonacci.Fibonacci";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpcfibonacci.Position,
      grpcfibonacci.Value> getComputeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "compute",
      requestType = grpcfibonacci.Position.class,
      responseType = grpcfibonacci.Value.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcfibonacci.Position,
      grpcfibonacci.Value> getComputeMethod() {
    io.grpc.MethodDescriptor<grpcfibonacci.Position, grpcfibonacci.Value> getComputeMethod;
    if ((getComputeMethod = FibonacciGrpc.getComputeMethod) == null) {
      synchronized (FibonacciGrpc.class) {
        if ((getComputeMethod = FibonacciGrpc.getComputeMethod) == null) {
          FibonacciGrpc.getComputeMethod = getComputeMethod =
              io.grpc.MethodDescriptor.<grpcfibonacci.Position, grpcfibonacci.Value>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "compute"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcfibonacci.Position.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcfibonacci.Value.getDefaultInstance()))
              .setSchemaDescriptor(new FibonacciMethodDescriptorSupplier("compute"))
              .build();
        }
      }
    }
    return getComputeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FibonacciStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FibonacciStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FibonacciStub>() {
        @java.lang.Override
        public FibonacciStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FibonacciStub(channel, callOptions);
        }
      };
    return FibonacciStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FibonacciBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FibonacciBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FibonacciBlockingStub>() {
        @java.lang.Override
        public FibonacciBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FibonacciBlockingStub(channel, callOptions);
        }
      };
    return FibonacciBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FibonacciFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FibonacciFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FibonacciFutureStub>() {
        @java.lang.Override
        public FibonacciFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FibonacciFutureStub(channel, callOptions);
        }
      };
    return FibonacciFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class FibonacciImplBase implements io.grpc.BindableService {

    /**
     */
    public void compute(grpcfibonacci.Position request,
        io.grpc.stub.StreamObserver<grpcfibonacci.Value> responseObserver) {
      asyncUnimplementedUnaryCall(getComputeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getComputeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcfibonacci.Position,
                grpcfibonacci.Value>(
                  this, METHODID_COMPUTE)))
          .build();
    }
  }

  /**
   */
  public static final class FibonacciStub extends io.grpc.stub.AbstractAsyncStub<FibonacciStub> {
    private FibonacciStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FibonacciStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FibonacciStub(channel, callOptions);
    }

    /**
     */
    public void compute(grpcfibonacci.Position request,
        io.grpc.stub.StreamObserver<grpcfibonacci.Value> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getComputeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class FibonacciBlockingStub extends io.grpc.stub.AbstractBlockingStub<FibonacciBlockingStub> {
    private FibonacciBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FibonacciBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FibonacciBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpcfibonacci.Value compute(grpcfibonacci.Position request) {
      return blockingUnaryCall(
          getChannel(), getComputeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class FibonacciFutureStub extends io.grpc.stub.AbstractFutureStub<FibonacciFutureStub> {
    private FibonacciFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FibonacciFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FibonacciFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpcfibonacci.Value> compute(
        grpcfibonacci.Position request) {
      return futureUnaryCall(
          getChannel().newCall(getComputeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_COMPUTE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final FibonacciImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(FibonacciImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_COMPUTE:
          serviceImpl.compute((grpcfibonacci.Position) request,
              (io.grpc.stub.StreamObserver<grpcfibonacci.Value>) responseObserver);
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

  private static abstract class FibonacciBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FibonacciBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpcfibonacci.FibonacciOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Fibonacci");
    }
  }

  private static final class FibonacciFileDescriptorSupplier
      extends FibonacciBaseDescriptorSupplier {
    FibonacciFileDescriptorSupplier() {}
  }

  private static final class FibonacciMethodDescriptorSupplier
      extends FibonacciBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    FibonacciMethodDescriptorSupplier(String methodName) {
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
      synchronized (FibonacciGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FibonacciFileDescriptorSupplier())
              .addMethod(getComputeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
