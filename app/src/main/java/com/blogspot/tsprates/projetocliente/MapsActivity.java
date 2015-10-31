package com.blogspot.tsprates.projetocliente;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //    private GoogleMap mMap;
    private GoogleMap mapa;
    private Telefone tel;
    private Messagem messagem;
    private Rastreador rastreador;


    private static final String URL_SERVIDOR = "http://tracking.comoj.com/postdata.php";
    private static final String API_KEY = "teste";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        tel = new Telefone(this);
        messagem = new Messagem(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.getUiSettings().setMyLocationButtonEnabled(true);

        messagem.mostra("AVISO", "Clique no marcador para enviar as coordenadas de longitude e latitude.");


        LatLng latLng = new LatLng(-16.7286406, -43.8582139);
        mapa.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        rastreador = new Rastreador(this, mapa, messagem, tel);
        rastreador.carregaLocationProvider();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rastreador != null) {
            rastreador.carregaLocationProvider();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (rastreador != null)
            rastreador.remove();

    }


}
