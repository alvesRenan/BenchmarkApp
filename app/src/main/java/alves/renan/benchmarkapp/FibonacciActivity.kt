package alves.renan.benchmarkapp

import alves.renan.benchmarkapp.fibonacci.FibonacciFactory
import alves.renan.benchmarkapp.fibonacci.FibonacciMpOS
import alves.renan.benchmarkapp.fibonacci.FibonacciStrategy
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import br.ufc.mdcc.mpos.MposFramework
import br.ufc.mdcc.mpos.config.Inject
import br.ufc.mdcc.mpos.config.MposConfig
import kotlinx.android.synthetic.main.activity_fibonacci.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

@MposConfig
class FibonacciActivity : AppCompatActivity() {

    @Inject(FibonacciMpOS::class)

    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fibonacci)

        val spinnerMethod: Spinner = findViewById(R.id.spin_method)
        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_method,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMethod.adapter = adapter
        }

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

        //FibonacciGRPCPBufServer.run()

        val n = input.text.toString().toInt()

        val methodName = spin_method.selectedItem.toString()
        val fibonacci : FibonacciStrategy = FibonacciFactory.getProcessMethod(methodName);

        /*
        The operation needs to be done in another thread
        to prevent 'android.os.NetworkOnMainThreadException'
        */
        doAsync {
            remoteExecutionTime.text = "Computing with $methodName..."

            fibonacci.preTest()
            val initTime = System.currentTimeMillis()
            val result = try {
                fibonacci.compFibonacci(n);
            } catch (e: Exception) {
                null
            }
            fibonacci.posTest()

            val endTime = System.currentTimeMillis() - initTime

            // puts the value on the UI thread
            uiThread {
                remoteExecutionTime.text = result?.toString() ?: "Error computing fibonacci"
            }
        }
    }
}

