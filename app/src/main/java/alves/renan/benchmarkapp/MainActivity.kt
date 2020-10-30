package alves.renan.benchmarkapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Launch the apps via intent
        val filter = IntentFilter().apply { addAction("benchmark.LAUNCH") }
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    launchActivity(
                        intent.getStringExtra("activity")
                    )
                }
            }
        }
        registerReceiver(receiver, filter)

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

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }

    private fun launchActivity(activityName: String) {
        val btn = when (activityName) {
            "matrix" -> btnMatrix
            "benchImage" -> btnBenchImage
            "fibonacci" -> btnFibonacci
            "nqueens" -> btnNQueens
            else -> null
        }

        btn?.performClick()
    }
}