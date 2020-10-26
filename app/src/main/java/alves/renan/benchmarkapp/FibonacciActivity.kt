package alves.renan.benchmarkapp

import alves.renan.benchmarkapp.fibonacci.Fibonacci
import alves.renan.benchmarkapp.fibonacci.FibonacciImpl
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.ufc.mdcc.mpos.MposFramework
import br.ufc.mdcc.mpos.config.Inject
import br.ufc.mdcc.mpos.config.MposConfig
import kotlinx.android.synthetic.main.activity_fibonacci.*
import org.jetbrains.anko.doAsync
import java.lang.Exception

@MposConfig
class FibonacciActivity : AppCompatActivity() {

    @Inject(FibonacciImpl::class)
    lateinit var fibonacci: Fibonacci

    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fibonacci)

        fibonacci = FibonacciImpl()
        startBtn.setOnClickListener { computeFib() }

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    val n = intent.getIntExtra("n", 1)
                    input.text = Editable.Factory.getInstance().newEditable( n.toString() )

                    startBtn.performClick()
                }
            }
        }

        // Filter for the BroadcastReceiver
        val filter = IntentFilter().apply {
            // Name of the action
            addAction("fibonacci.EXTRAS")
        }

        // Set the receiver
        registerReceiver(receiver, filter)

        val extras = intent.extras
        if (extras != null) {
            if (extras.containsKey("cloudlet")) {
                val cloudlet = extras.getString("cloudlet")
                Log.d(this.packageName, "Extras: cloudlet = $cloudlet")

                MposFramework.getInstance().start(this, cloudlet)
            }
        } else {
            Log.i("EXTRAS", "No extras received.")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun computeFib() {
        val n = input.text.toString().toInt()

        doAsync {
            remoteExecutionTime.text = "Computing..."

            val initTime = System.currentTimeMillis()

            val result = try {
                fibonacci.compFibonacci(n)
            } catch (e: Exception) {
                null
            }

            val endTime = System.currentTimeMillis() - initTime

            remoteExecutionTime.text = result?.let {
                "Value: $it ($endTime ms)"
            } ?: {
                "Error computing fibonacci"
            }.toString()
        }
    }
}