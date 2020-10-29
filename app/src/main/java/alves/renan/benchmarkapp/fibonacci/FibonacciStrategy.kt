package alves.renan.benchmarkapp.fibonacci

import br.ufc.mdcc.mpos.offload.Remotable

interface FibonacciStrategy {

    // METODO PARA IMPLEMENTAR TUDO AQUILO QUE E NECESSARIO ANTES DO PROCESSAMENTO DA FUNCAO
    fun preTest()

    @Remotable(status = true, value = Remotable.Offload.STATIC)
    fun compFibonacci(n: Int): Int

    // METODO PARA IMPLEMENTAR TUDO AQUILO QUE E NECESSARIO DEPOIS DO PROCESSAMENTO DA FUNCAO
    fun posTest()
}