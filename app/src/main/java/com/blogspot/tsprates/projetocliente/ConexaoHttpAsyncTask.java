package com.blogspot.tsprates.projetocliente;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe assíncrona de conexão com servidor remoto.
 *
 * @author Thiago
 */
public class ConexaoHttpAsyncTask extends AsyncTask<String, Void, String> {
    private ProgressDialog carregando;
    private Context contexto;

    /**
     * Constructor.
     *
     * @param c
     */
    public ConexaoHttpAsyncTask(Context c) {
        contexto = c;
        carregando = new ProgressDialog(c);
    }

    protected void onPostExecute(String result) {
        carregando.dismiss();

        // verifica resposta do servidor
        String strResult, json = null;

        if (result != null) {
            strResult = result.trim().replace("<[^>]*>", "");
            try {
                json = (String) new JSONObject(strResult).get("result");
            } catch (JSONException e) {
            }
        }

        String msg = ((Boolean.parseBoolean(json))
                ? "Coordenadas enviada com sucesso."
                : "Ocorreu um erro.");


        Toast.makeText(contexto, msg, Toast.LENGTH_LONG).show();
    }

    protected void onPreExecute() {
        carregando.setTitle("Por favor, aguarde um instante.");
        carregando.setMessage("Carregando...");
        carregando.setCancelable(false);
        carregando.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        Map<String, String> parametros = new HashMap<String, String>();

        parametros.put("latitude", params[1]);
        parametros.put("longitude", params[2]);
        parametros.put("telefone", params[3]);
        parametros.put("api", params[4]);

        return ConexaoHttp.em(url, parametros);
    }

}
