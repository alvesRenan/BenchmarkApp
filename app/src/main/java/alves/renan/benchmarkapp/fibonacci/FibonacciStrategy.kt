package alves.renan.benchmarkapp.fibonacci

import br.ufc.mdcc.mpos.offload.Remotable

interface FibonacciStrategy {

    // TODO: COLOCAR UM METODO INIT (O QUE FAZER ANTES DO PROCESSAMENTO? ABRIR CHANNEL, POR EX?)

    @Remotable(status = true, value = Remotable.Offload.STATIC)
    fun compFibonacci(n: Int): Int

    // TODO: COLOCAR UM METODO END (O QUE FAZER DEPOIS DO PROCESSAMENTO? FECHAR CHANNEL, POR EX?)
}