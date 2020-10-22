package alves.renan.benchmarkapp.matrixoperations

import br.ufc.mdcc.mpos.offload.Remotable

interface Matrix {

    fun random(m :Int, n: Int): Array<DoubleArray>

    @Remotable( status = true, value = Remotable.Offload.STATIC )
    fun add(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray>

    @Remotable( status = true, value = Remotable.Offload.STATIC )
    fun multiply(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray>
}