package alves.renan.benchmarkapp.fibonacci;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class FibonacciGRPCPBuf implements FibonacciStrategy {

    private ManagedChannel channel;
    private grpcfibonacci.FibonacciGrpc.FibonacciBlockingStub stub;
    private FibonacciGRPCPBufServer server;

    private String serverIp;
    private int serverPort;
    private boolean serverMode;

    public FibonacciGRPCPBuf(String host, int port, boolean actServer) {
        serverIp = host;
        serverPort = port;
        serverMode = actServer;
    }

    @Override
    public void preTest() {
        /*if (serverMode) {
            server = new FibonacciGRPCPBufServer();
        } else { // Client Mode
            channel = ManagedChannelBuilder.forAddress(serverIp, serverPort).usePlaintext().build();
            stub = grpcfibonacci.FibonacciGrpc.newBlockingStub(channel);
        }*/
        channel = ManagedChannelBuilder.forAddress(serverIp, serverPort).usePlaintext().build();
        stub = grpcfibonacci.FibonacciGrpc.newBlockingStub(channel);
    }

    @Override
    public void posTest() {
        /*if (serverMode) {
            try {
                server.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            channel.shutdownNow();
        }*/
        channel.shutdownNow();
    }
    
    @Override
    public int compFibonacci(int n) {
        grpcfibonacci.Position request = grpcfibonacci.Position.newBuilder().setNumber(n).build();
        grpcfibonacci.Value response = stub.compute(request);

        return response.getNumber();
    }
}
