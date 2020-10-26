package alves.renan.benchmarkapp.fibonacci

import br.ufc.mdcc.mpos.offload.Remotable

interface Fibonacci {

    @Remotable(status = true, value = Remotable.Offload.STATIC)
    fun compFibonacci(n: Int): Int
}