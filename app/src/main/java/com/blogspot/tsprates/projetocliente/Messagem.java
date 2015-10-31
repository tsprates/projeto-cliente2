package com.blogspot.tsprates.projetocliente;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Messagem {
    private Activity activity;

    public Messagem(Activity a) {
        this.activity = a;
    }

    public void mostra(String title, String msg) {
        (new AlertDialog.Builder(activity)).setTitle(title)
                .setMessage(msg)
                .setNeutralButton("Fechar", new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}
