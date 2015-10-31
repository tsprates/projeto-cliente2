package com.blogspot.tsprates.projetocliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Classe de conexão HTTP.
 *
 * @author Thiago
 */
public class ConexaoHttp {
    private static URL baseUrl = null;

    private static HttpURLConnection http = null;

    private static StringBuilder resultado = null;

    /**
     * Método que envia as coordenadas ao servidor.
     *
     * @param url
     * @param parametros
     * @return
     */
    public static String em(String url, Map<String, String> parametros) {
        resultado = new StringBuilder();

        try {
            baseUrl = new URL(url);
            http = (HttpURLConnection) baseUrl.openConnection();

            // http query builder
            String params = "";
            Set<String> paramsKey = parametros.keySet();

            for (String key : paramsKey) {
                params += String.format("&%s=%s", key, URLEncoder.encode(parametros.get(key), "UTF-8"));
            }

            if (params.length() > 0) {
                params = params.substring(1);
            }

            http.setDoInput(true);
            http.setDoOutput(true);

            // http headers
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            http.setRequestProperty("Content-Length",
                    Integer.toString(params.getBytes().length));

            // enviando dados
            OutputStream out = http.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            out.close();

            // retorno
            String linha;
            InputStreamReader leitor = new InputStreamReader(
                    http.getInputStream());
            BufferedReader buffer = new BufferedReader(leitor);
            while ((linha = buffer.readLine()) != null) {
                resultado.append(linha);
                resultado.append('\r');
            }
            buffer.close();

            if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "ERROR";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }

        return resultado.toString();
    }
}
