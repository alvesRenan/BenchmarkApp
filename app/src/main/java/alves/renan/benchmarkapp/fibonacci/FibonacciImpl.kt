package alves.renan.benchmarkapp.fibonacci

class FibonacciImpl : Fibonacci {

    override fun compFibonacci(n: Int): Int {
        return if (n <= 2) {
            1
        } else {
            compFibonacci(n - 2 ) + compFibonacci(n - 1)
        }
    }
}