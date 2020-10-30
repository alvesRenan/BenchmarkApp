package alves.renan.benchmarkapp.fibonacci;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import grpcfibonacci.FibonacciGrpc;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;

public class FibonacciGRPCPBufServer {
    private final int port = 50051;
    private Server server;

    public void start() throws IOException {
        /* The port on which the server should run */
        server = NettyServerBuilder.forPort(port).addService(new FibonacciServiceImpl()).build().start();
        System.out.println("Server started, listening on " + port);
        System.out.println("RPCMethod,Language,Operation,OpTime,TotalTime");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    FibonacciGRPCPBufServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void run() throws IOException, InterruptedException {
        final FibonacciGRPCPBufServer server = new FibonacciGRPCPBufServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class FibonacciServiceImpl extends FibonacciGrpc.FibonacciImplBase {
        private int compFibonacci(int n) {
            Integer value = new Integer(1);

            if (n > 2) {
                value = new Integer(compFibonacci(n - 2) + compFibonacci(n - 1));
            }

            return value;
        }

        @Override
        public void compute(grpcfibonacci.Position request, StreamObserver<grpcfibonacci.Value> responseObserver) {

            long init_proc = System.nanoTime();

            int fibonacciPos = request.getNumber();
            int fibonacciVal = compFibonacci(fibonacciPos);
            grpcfibonacci.Value reply = grpcfibonacci.Value.newBuilder().setNumber(fibonacciVal).build();
            responseObserver.onNext(reply);

            long end_proc = System.nanoTime();

            System.out.println("gRPCProto,Java," + ((end_proc - init_proc) / 1000000));

            responseObserver.onCompleted();

        }
    }
}
