package alves.renan.benchmarkapp;

import android.app.Activity;

public class AppsFactory {

    // PASSO 1: Criar a classe da Activity e o XML com o Layout
    // PASSO 1.1: Registrar a classe no AndroidManifest

    public static Activity getAppActivity(String app_name) {
        Activity appActivity = null;

        if (app_name.equals("MatrixApp")) {
            appActivity = new MatrixActivity();
        } else if (app_name.equals("BenchImageApp")){
            appActivity = new BenchImageActivity();
        } else if (app_name.equals("FibonacciApp")){
            appActivity = new FibonacciActivity();
        } else if (app_name.equals("NQueensApp")){
            appActivity = new NQueensActivity();
        } // PASSO 3: Adicionar a instanciacao da Activity

        return appActivity;
    }
}
