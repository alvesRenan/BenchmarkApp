package alves.renan.benchmarkapp.fibonacci;

import alves.renan.benchmarkapp.fibonacci.grpcfibonacci.FibonacciCAOS;

public class FibonacciFactory {

    public static FibonacciStrategy getProcessMethod(String method) {
        FibonacciStrategy strategy = null;

        if (method.equals("gRPC/Proto")) {
            strategy = new FibonacciGRPCPBuf("192.168.1.5", 50051);
        } else if (method.equals("Local")) {
            strategy = new FibonacciLocal();
        } else if (method.equals("MpOS")) {
            strategy = new FibonacciMpOS();
        } else if (method.equals("CAOS")) { /* PASSO 3: VINCULAR O VALOR DO SPINNER A NOVA ESTRATEGIA*/
            strategy = new FibonacciCAOS();
        }

        return strategy;
    }
}
