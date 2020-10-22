package alves.renan.benchmarkapp;

import java.io.File;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import alves.renan.benchmarkapp.benchimage.dao.model.AppConfiguration;
import alves.renan.benchmarkapp.benchimage.dao.model.ResultImage;
import alves.renan.benchmarkapp.benchimage.image.CloudletFilter;
import alves.renan.benchmarkapp.benchimage.image.Filter;
import alves.renan.benchmarkapp.benchimage.image.ImageFilter;
import alves.renan.benchmarkapp.benchimage.image.ImageFilterTask;
import alves.renan.benchmarkapp.benchimage.image.InternetFilter;
import alves.renan.benchmarkapp.benchimage.util.ExportData;
import alves.renan.benchmarkapp.benchimage.util.TaskResultAdapter;
import br.ufc.mdcc.mpos.MposFramework;
import br.ufc.mdcc.mpos.config.Inject;
import br.ufc.mdcc.mpos.config.MposConfig;

@SuppressLint("SetTextI18n")

@MposConfig()
public final class BenchImageActivity extends Activity {
    int extraSize, extraFilter, extraLocal;
    Bundle extras = null;
    BroadcastReceiver receiver;

    private final String clsName = MainActivity.class.getName();

    private Filter filterLocal = new ImageFilter();

    @Inject(ImageFilter.class)
    private CloudletFilter cloudletFilter;

    @Inject(ImageFilter.class)
    private InternetFilter internetFilter;

    private AppConfiguration config;
    private String photoName;
    private long vmSize = 0L;

    private boolean quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchimage);

        quit = false;
        config = new AppConfiguration();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                extras = intent.getExtras();

                if (extras != null) {
                    // Set the values received via intent
                    if (extras.containsKey("size"))
                        extraSize = extras.getInt("size");
                    if (extras.containsKey("filter"))
                        extraFilter = extras.getInt("filter");
                    if (extras.containsKey("local"))
                        extraLocal = extras.getInt("local");
                }

                configureSpinner();
                findViewById(R.id.button_execute).performClick();
            }
        };

        // Receive cloudlet IP via intent
        Bundle cloudletExtra = getIntent().getExtras();
        if (cloudletExtra != null) {
            if (cloudletExtra.containsKey("cloudlet")) {
                String cloudlet = cloudletExtra.getString("cloudlet");
                Log.i(clsName, "Extra Cloudlet: " + cloudlet);

                MposFramework.getInstance().start(this, cloudlet);
            }
        }

        configureSpinner();
        getConfigFromSpinner();

        configureButton();
        configureStatusView("Status: Sem Atividade");

        createDirOutput();
        processImage();

        Log.i(clsName, "Iniciou PicFilter");

        IntentFilter filter = new IntentFilter(); // Set the action filter
        filter.addAction("benchimage2.EXTRAS");

        registerReceiver(receiver, filter); // Finally, register the filter
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);

        if (quit) {
            try {
                MposFramework.getInstance().stop();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            Process.killProcess(Process.myPid());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        switch (item.getItemId()) {
            case R.id.menu_action_export:
                alertDialogBuilder.setTitle("Exportar Resultados");
                alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialogBuilder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new ExportData(BenchImageActivity.this, "benchimage2_data.csv").execute();
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.setMessage("Deseja exportar resultados?");
                alertDialogBuilder.create().show();
                break;
        }

        return true;
    }

    private void processImage() {
        getConfigFromSpinner();
        configureStatusViewOnTaskStart();

        System.gc();

        // O BroadcastReceiver chama esse método duas vezes, sendo que na primeira o filter vem null
        if (cloudletFilter != null || filterLocal != null) {
            if ((config.getFilter().equals("Cartoonizer") || config.getFilter().equals("Benchmark")) && vmSize <= 64 && (config.getSize().equals("8MP") || config.getSize().equals("4MP"))) {
                dialogSupportFilter();
            } else {
                switch (config.getLocal()) {
                    case "Local":
                        new ImageFilterTask(getApplication(), filterLocal, config, taskResultAdapter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        break;
                    case "Cloudlet":
                        new ImageFilterTask(getApplication(), cloudletFilter, config, taskResultAdapter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        break;
                    default:
                        new ImageFilterTask(getApplication(), internetFilter, config, taskResultAdapter).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        break;
                }
            }
        }
    }

    private void configureButton() {
        Button but = (Button) findViewById(R.id.button_execute);
        but.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStatusChange(R.id.button_execute, false, "Processando");
                processImage();
            }
        });
    }

    private void configureSpinner() {
        Spinner spinnerImage = (Spinner) findViewById(R.id.spin_image);
        Spinner spinnerFilter = (Spinner) findViewById(R.id.spin_filter);
        Spinner spinnerSize = (Spinner) findViewById(R.id.spin_size);
        Spinner spinnerLocal = (Spinner) findViewById(R.id.spin_local);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_img, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerImage.setAdapter(adapter);
        spinnerImage.setSelection(2);

        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_filter, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_local, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocal.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.spinner_size, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapter);
        spinnerSize.setSelection(4);

        // If it has extras we change the set default values of the spinners
        if (extras != null) {
            spinnerSize.setSelection(extraSize);
            spinnerFilter.setSelection(extraFilter);
            spinnerLocal.setSelection(extraLocal);
        }
    }

    private void configureStatusViewOnTaskStart() {
        configureStatusView("Status: Submetendo Tarefa");

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(null);
    }

    private void configureStatusView(String status) {
        TextView tv_vmsize = (TextView) findViewById(R.id.text_vmsize);
        vmSize = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        tv_vmsize.setText("VMSize " + vmSize + "MB");

        if (vmSize < 128) {
            tv_vmsize.setTextColor(Color.RED);
        } else if (vmSize == 128) {
            tv_vmsize.setTextColor(Color.YELLOW);
        } else {
            tv_vmsize.setTextColor(Color.GREEN);
        }

        TextView tv_execucao = (TextView) findViewById(R.id.text_exec);
        tv_execucao.setText("Tempo de\nExecução: 0s");

        TextView tv_tamanho = (TextView) findViewById(R.id.text_size);
        tv_tamanho.setText("Tamanho/Foto: " + config.getSize() + "/" + photoName);

        TextView tv_status = (TextView) findViewById(R.id.text_status);
        tv_status.setText(status);
    }

    private void getConfigFromSpinner() {
        Spinner spinnerImage = (Spinner) findViewById(R.id.spin_image);
        Spinner spinnerFilter = (Spinner) findViewById(R.id.spin_filter);
        Spinner spinnerSize = (Spinner) findViewById(R.id.spin_size);
        Spinner spinnerLocal = (Spinner) findViewById(R.id.spin_local);

        photoName = (String) spinnerImage.getSelectedItem();
        config.setImage(photoNameToFileName(photoName));
        config.setLocal((String) spinnerLocal.getSelectedItem());

        config.setFilter((String) spinnerFilter.getSelectedItem());
        if (config.getFilter().equals("Benchmark")) {
            config.setSize("All");
        } else {
            config.setSize((String) spinnerSize.getSelectedItem());
        }

        Log.d("getConfigFromSpinners", String.format(
                "photo: %s - local: %s - filter: %s", config.getImage(), config.getLocal(), config.getFilter()
        ));
    }

    private void dialogSupportFilter() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Celular limitado!");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setNegativeButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.setMessage("Celular não suporta o Cartoonizer, minimo recomendo é 128MB de VMSize");
        // cria e mostra
        alertDialogBuilder.create().show();

        buttonStatusChange(R.id.button_execute, true, "Inicia");
        TextView tv_status = (TextView) findViewById(R.id.text_status);
        tv_status.setText("Status: Requisição anterior não suporta Filtro!");
    }

    private void buttonStatusChange(int id, boolean state, String text) {
        Button but = (Button) findViewById(id);
        but.setEnabled(state);
        but.setText(text);
    }

    private String photoNameToFileName(String name) {
        if (name.equals("FAB Show")) {
            return "img1.jpg";
        } else if (name.equals("Cidade")) {
            return "img4.jpg";
        } else if (name.equals("SkyLine")) {
            return "img5.jpg";
        }
        return null;
    }

    private void createDirOutput() {
        File storage = Environment.getExternalStorageDirectory();
        String outputDir = storage.getAbsolutePath() + File.separator + "BenchImageOutput";

        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        config.setOutputDirectory(outputDir);
    }

    private TaskResultAdapter<ResultImage> taskResultAdapter = new TaskResultAdapter<ResultImage>() {
        @Override
        public void completedTask(ResultImage obj) {
            if (obj != null) {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(obj.getBitmap());

                TextView tv_tamanho = (TextView) findViewById(R.id.text_size);
                tv_tamanho.setText("Tamanho/Foto: " + config.getSize() + "/" + photoName);

                TextView tv_execucao = (TextView) findViewById(R.id.text_exec);
                if (obj.getTotalTime() != 0) {
                    double segundos = obj.getTotalTime() / 1000.0;
                    tv_execucao.setText("Tempo de\nExecução: " + String.format("%.3f", segundos) + "s");
                } else {
                    tv_execucao.setText("Tempo de\nExecução: 0s");
                }
                if (obj.getConfig().getFilter().equals("Benchmark")) {
                    double segundos = obj.getTotalTime() / 1000.0;
                    tv_execucao.setText("Tempo de\nExecução: " + String.format("%.3f", segundos) + "s");
                }
            } else {
                TextView tv_status = (TextView) findViewById(R.id.text_status);
                tv_status.setText("Status: Algum Error na transmissão!");
            }
            buttonStatusChange(R.id.button_execute, true, "Inicia");
        }

        @Override
        public void taskOnGoing(int completed, String statusText) {
            TextView tv_status = (TextView) findViewById(R.id.text_status);
            tv_status.setText("Status: " + statusText);
        }
    };
}