package alves.renan.benchmarkapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout : LinearLayout = findViewById(R.id.MainLinearLayout)

        val apps_names = resources.getStringArray(R.array.apps_names).iterator()
        apps_names.forEach {
            var chkBox = RadioButton(this)
            chkBox.setLayoutParams(
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT))
            chkBox.setPadding(chkBox.getPaddingLeft() + 42,
                chkBox.getPaddingTop() + 42, chkBox.getPaddingRight() + 42,chkBox.getPaddingBottom() + 42)
            chkBox.setText("$it")

            layout.addView(chkBox)
        }
    }

    fun launchApp(view: View?) {
        val rb = findViewById(R.id.MainLinearLayout) as RadioGroup
        val app_name = resources.getStringArray(R.array.apps_names)[rb.checkedRadioButtonId-1]

        val app_activity = AppsFactory.getAppActivity(app_name)
        if (app_activity == null) {
            Toast.makeText(this, "Activity not found", Toast.LENGTH_LONG).show()
        } else {
            startActivity( Intent(applicationContext, app_activity::class.java) )
        }
    }

}