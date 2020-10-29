package alves.renan.benchmarkapp.fibonacci;

public class FibonacciLocal implements FibonacciStrategy {

    @Override
    public int compFibonacci(int n) {
        Integer value = new Integer(1);

        if (n > 2) {
            value = new Integer(compFibonacci(n - 2) + compFibonacci(n - 1));
        }

        return value;
    }
}
