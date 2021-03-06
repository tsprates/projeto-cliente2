package com.blogspot.tsprates.projetocliente;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;


public class Rastreador implements View.OnClickListener, LocationListener, GoogleMap.OnMarkerClickListener {
    private Activity activity;
    private GoogleMap mapa;
    private LocationManager locManager;
    private long tempoDeAtualizacao = 5000;
    private float distanciaMinima = 10.0f;
    private Double latitude;
    private Double longitude;
    private Mensagem msg;
    private Telefone tel;
    private Marker marcador;

    public Rastreador(Activity a, GoogleMap mapa, Mensagem msg, Telefone tel) {
        this.mapa = mapa;
        this.activity = a;
        this.msg = msg;
        this.tel = tel;

        this.locManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }


    public void carregaLocationProvider() {

        boolean isEnabledNetWork = false;
        boolean isEnabledGPS = false;

        if (this.locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            this.locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    this.tempoDeAtualizacao, this.distanciaMinima, this);
            isEnabledNetWork = true;
        }

        if (this.locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !isEnabledNetWork) {
            this.locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, this.tempoDeAtualizacao, this.distanciaMinima, this);
            isEnabledGPS = true;
        }


        if (!isEnabledGPS && !isEnabledNetWork) {
            msg.mostra("Erro de Location Provider", "Desculpe, mas não foi possível carregar o ratreador.");

//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            activity.startActivity(intent);
        }

    }


    public void carregaLocationProvider2() {
        // http://www.vogella.com/tutorials/AndroidLocationAPI/article.html
        Criteria criteria = new Criteria();
        String provider = this.locManager.getBestProvider(criteria, false);
        Location location = this.locManager.getLastKnownLocation(provider);

        this.locManager.requestLocationUpdates(provider, this.tempoDeAtualizacao, this.distanciaMinima, this);

        if (location != null) {
            _moveCameraPara(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }


    public void onLocationChanged(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();

        LatLng ponto = new LatLng(this.latitude.doubleValue(), this.longitude.doubleValue());
        _moveCameraPara(ponto);
    }

    private void _moveCameraPara(LatLng ponto) {
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(ponto, 15.0F);
        this.mapa.animateCamera(camera);
        this._adicionaMarcador(ponto);
    }

    private void _adicionaMarcador(LatLng ponto) {
        String endereco = this.getEnderecoDoPonto(ponto);
        if (endereco != null) {
            MarkerOptions marcador = (new MarkerOptions())
                    .position(ponto)
                    .title("Clique no marcador para enviar sua posição.")
                    .snippet(endereco);
            this.mapa.clear();

            this.marcador = this.mapa.addMarker(marcador);
            this.marcador.showInfoWindow();

            this.mapa.setOnMarkerClickListener(this);
        }
    }

    public String getEnderecoDoPonto(LatLng ponto) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            List enderecos = geocoder.getFromLocation(ponto.latitude, ponto.longitude, 1);
            Address address = (Address) enderecos.get(0);
            return address.getAddressLine(0);
        } catch (Exception ex) {
            return null;
        }
    }

    public void onProviderDisabled(String provider) {
        msg.mostra("Erro", "Desculpe, mas não foi possível carregar o GPS.");
    }

    public void onProviderEnabled(String provider) {
        msg.mostra("AVISO", "Clique no marcador para enviar as coordenadas de longitude e latitude.");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        this.carregaLocationProvider2();
    }

    public void remove() {
        this.locManager.removeUpdates(this);
    }

    public boolean onMarkerClick(Marker marker) {
        this._fazCheckIn();
        return true;
    }

    private void _fazCheckIn() {
        if (this.latitude == null && this.longitude == null) {
            msg.mostra("Erro", "Desculpe, não foi possível obter a longitude e latitude.");
        } else {
            (new ConexaoHttpAsyncTask(activity)).execute(Config.URL_SERVIDOR,
                    "" + this.latitude,
                    "" + this.longitude,
                    tel.getIMEI(),
                    Config.API_KEY);

            if (marcador != null && !marcador.isInfoWindowShown()) {
                marcador.showInfoWindow();
            }
        }

    }

    @Override
    public void onClick(View v) {
        this._fazCheckIn();
    }
}

