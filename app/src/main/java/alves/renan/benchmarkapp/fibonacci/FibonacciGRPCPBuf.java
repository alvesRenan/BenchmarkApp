package alves.renan.benchmarkapp.fibonacci;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class FibonacciGRPCPBuf implements FibonacciStrategy {

    private ManagedChannel channel;
    grpcfibonacci.FibonacciGrpc.FibonacciBlockingStub stub;

    public FibonacciGRPCPBuf(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        stub = grpcfibonacci.FibonacciGrpc.newBlockingStub(channel);
    }
    
    @Override
    public int compFibonacci(int n) {
        grpcfibonacci.Position request = grpcfibonacci.Position.newBuilder().setNumber(n).build();
        grpcfibonacci.Value response = stub.compute(request);
        channel.shutdownNow();

        return response.getNumber();
    }
}
