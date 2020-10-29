package alves.renan.benchmarkapp.fibonacci.grpcfibonacci;

import alves.renan.benchmarkapp.fibonacci.FibonacciStrategy;

/* PASSO 1: IMPLEMENTAR A ESTRATEGIA */
public class FibonacciCAOS implements FibonacciStrategy {

    @Override
    public int compFibonacci(int n) {
        System.out.println("VOU PROCESAR ALGUMA COISA!!!!");
        return 42;
    }
}

