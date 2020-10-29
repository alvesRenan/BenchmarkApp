package alves.renan.benchmarkapp.fibonacci;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class FibonacciGRPCPBuf implements FibonacciStrategy {

    private ManagedChannel channel;
    grpcfibonacci.FibonacciGrpc.FibonacciBlockingStub stub;

    private String serverIp;
    private int serverPort;

    public FibonacciGRPCPBuf(String host, int port) {
        serverIp = host;
        serverPort = port;
    }

    @Override
    public void preTest() {
        channel = ManagedChannelBuilder.forAddress(serverIp, serverPort).usePlaintext().build();
        stub = grpcfibonacci.FibonacciGrpc.newBlockingStub(channel);
    }

    @Override
    public void posTest() {
        channel.shutdownNow();
    }
    
    @Override
    public int compFibonacci(int n) {
        grpcfibonacci.Position request = grpcfibonacci.Position.newBuilder().setNumber(n).build();
        grpcfibonacci.Value response = stub.compute(request);

        return response.getNumber();
    }
}
