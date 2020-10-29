package alves.renan.benchmarkapp.fibonacci

class FibonacciMpOS : FibonacciStrategy {

    override fun preTest() {
        println("preTest")
    }

    override fun posTest() {
        println("posTest")
    }
    override fun compFibonacci(n: Int): Int {
        return if (n <= 2) {
            1
        } else {
            compFibonacci(n - 2 ) + compFibonacci(n - 1)
        }
    }
}