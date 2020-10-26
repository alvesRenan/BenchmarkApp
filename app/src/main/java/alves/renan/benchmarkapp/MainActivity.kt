package alves.renan.benchmarkapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMatrix.setOnClickListener {
            startActivity( Intent(applicationContext, MatrixActivity::class.java) )
        }

        btnBenchImage.setOnClickListener {
            startActivity( Intent(applicationContext, BenchImageActivity::class.java) )
        }

        btnFibonacci.setOnClickListener {
            startActivity( Intent(applicationContext, FibonacciActivity::class.java) )
        }

        btnNQueens.setOnClickListener {
            startActivity( Intent(applicationContext, NQueensActivity::class.java) )
        }
    }
}