package com.blogspot.tsprates.projetocliente;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

public class Telefone {

    private String numeroTelefone;

    public Telefone(Activity a) {
        TelephonyManager telManager = (TelephonyManager) a.getSystemService(Context.TELEPHONY_SERVICE);
        this.numeroTelefone = telManager.getDeviceId();
    }

    public String getIMEI() {
        return numeroTelefone;
    }
}
