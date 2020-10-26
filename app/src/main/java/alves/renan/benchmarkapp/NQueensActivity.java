package alves.renan.benchmarkapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;

import alves.renan.benchmarkapp.nqueens.NQueens;
import alves.renan.benchmarkapp.nqueens.NQueensBacktracking;

public class NQueensActivity extends Activity {
    private NQueens nq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nqueens);

        nq = new NQueensBacktracking();

        NumberPicker np = (NumberPicker) findViewById(R.id.timePicker1);
        np.setMaxValue(14);
        np.setMinValue(4);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("SimpleDateFormat")
    public void setExecTime(long totalTime) {
        Date date = new Date(totalTime);
        SimpleDateFormat df = new SimpleDateFormat("mm:ss.SSS");
        ((TextView) findViewById(R.id.exectime)).setText("Execution Time: "
                + df.format(date));
    }

    public void resetExecTime() {
        ((TextView) findViewById(R.id.exectime)).setText("Execution Time: 00:00.000");
    }

    public void setCalcResult(int value) {
        ((TextView) findViewById(R.id.result)).setText("Result: " + value + " solutions");
    }

    public void resetCalcResult() {
        ((TextView) findViewById(R.id.result)).setText("Result: 0 solutions");
    }

    public void disableButton() {
        ((Button) findViewById(R.id.button1)).setText("Calculating...");
        ((Button) findViewById(R.id.button1)).setClickable(false);
    }

    public void enableButton() {
        ((Button) findViewById(R.id.button1))
                .setText("Calc Number of Solutions");
        ((Button) findViewById(R.id.button1)).setClickable(true);
    }

    public boolean isBenchmarkSeted(){
        return ((CheckBox) findViewById(R.id.benchmarkcheckBox)).isChecked();
    }

    /*
     * On Click event button
     */
    public void calc(View view) {
        int n = ((NumberPicker) findViewById(R.id.timePicker1)).getValue();
        disableButton();
        new CalcTask().execute(n);
    }

    private class CalcTask extends AsyncTask<Integer, Void, Integer> {

        long initialTime = 0;
        long totalTime = 0;
        int res = 0;

        @Override
        protected Integer doInBackground(Integer... values) {
            initialTime = System.currentTimeMillis();
            // Compute nquens
            if (!isBenchmarkSeted()){
                res = nq.callplaceNqueens(values[0]);
            }else{
                for (int i = 8; i <= 14; i++) {
                    for (int j = 1; j <= 5; j++) {
                        res = nq.callplaceNqueens(i);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            totalTime = System.currentTimeMillis() - initialTime;
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            enableButton();
            setCalcResult(res);
            setExecTime(totalTime);
            Log.i("resultado:", Long.toString(totalTime));
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Close");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setMessage("Close NQueens?");
        alertDialogBuilder.create().show();
    }
}
