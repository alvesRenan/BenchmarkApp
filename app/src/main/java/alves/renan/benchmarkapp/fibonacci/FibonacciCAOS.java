package alves.renan.benchmarkapp.fibonacci;

import alves.renan.benchmarkapp.fibonacci.FibonacciStrategy;

/* PASSO 1: IMPLEMENTAR A ESTRATEGIA */
public class FibonacciCAOS implements FibonacciStrategy {

    @Override
    public void preTest() { System.out.println("preTest"); }

    @Override
    public void posTest() { System.out.println("posTest"); }

    @Override
    public int compFibonacci(int n) {
        System.out.println("VOU PROCESAR ALGUMA COISA!!!!");
        return 42;
    }
}

