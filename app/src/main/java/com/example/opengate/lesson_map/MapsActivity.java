package com.example.opengate.lesson_map;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /* declaration */
    private GoogleMap mMap;
    private TextView str_loc;
    private Button btn_go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* variable initialization */
        str_loc = (TextView) findViewById(R.id.str_loc);
        btn_go_back = (Button) findViewById(R.id.btn_go_back);
        btn_go_back.setOnClickListener(btnBackClick);
    }

    private View.OnClickListener btnBackClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent it = new Intent();
            it.setClass(MapsActivity.this, ViewList.class);
            startActivity(it);
        }
    };

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
        mMap = googleMap;

        Bundle bundle = getIntent().getExtras();
        String iSelectLoc = bundle.getString("SELECT_LOC");
        double iSelectLat = bundle.getDouble("SELECT_LAT");
        double iSelectLon = bundle.getDouble("SELECT_LON");
        LatLng curLoc = new LatLng(iSelectLat, iSelectLon);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 15.0f));
        mMap.addMarker(new MarkerOptions().position(curLoc).title(iSelectLoc).snippet("your favorite city"));

        str_loc.setText(iSelectLoc + "(" + String.valueOf(iSelectLat) + "," + String.valueOf(iSelectLon) + ")");
    }
}
