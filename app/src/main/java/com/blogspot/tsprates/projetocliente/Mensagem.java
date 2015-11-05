package com.blogspot.tsprates.projetocliente;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Mensagem {
    private Activity activity;

    public Mensagem(Activity a) {
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

    private boolean result = false;

    public boolean confirma(String titulo, String msg) {
        result = false;

        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setTitle(titulo);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                Mensagem.this.result = true;
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                Mensagem.this.result = false;
                dialog.dismiss();
            }
        });

        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();

        return this.result;
    }
}
