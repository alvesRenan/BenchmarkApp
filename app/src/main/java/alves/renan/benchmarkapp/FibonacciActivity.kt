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
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import br.ufc.mdcc.mpos.MposFramework
import br.ufc.mdcc.mpos.config.Inject
import br.ufc.mdcc.mpos.config.MposConfig
import kotlinx.android.synthetic.main.activity_fibonacci.*
import org.jetbrains.anko.doAsync
import java.util.*

@MposConfig
class FibonacciActivity : AppCompatActivity() {

    @Inject(FibonacciMpOS::class)
    lateinit var fibonacci: FibonacciStrategy

    private lateinit var receiver: BroadcastReceiver

    //private var spinnerMethod : Spinner? = null;

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

        /*spinnerMethod = this.spin_method
        val aa = ArrayAdapter.createFromResource(this, R.array.spinner_method, android.R.layout.simple_spinner_item)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMethod!!.setAdapter(aa)*/

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
        val name_method = spin_method.selectedItem.toString()

        //doAsync {
        remoteExecutionTime.text = "Computing with $name_method..."

        val initTime = System.currentTimeMillis()

        val result = try {
            FibonacciFactory.getProcessMethod(name_method).compFibonacci(n);
        } catch (e: Exception) {
            null
        }
gi
        val endTime = System.currentTimeMillis() - initTime

        remoteExecutionTime.text = result?.let {
            "Value: $it ($endTime ms)"
        } ?: {
            "Error computing fibonacci"
        }.toString()
        //}
    }
}

