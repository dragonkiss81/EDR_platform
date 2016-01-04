package com.example.opengate.lesson_map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;

        Bundle bundle = getIntent().getExtras();
        final String iSelectName = bundle.getString("SELECT_NAME");
        final String iSelectId = bundle.getString("SELECT_ID");
        final String iSelectPath = bundle.getString("SELECT_PATH");
        final String iSelectTime = bundle.getString("SELECT_TIME");
        final String iSelectOwner = bundle.getString("SELECT_OWNER");
        final String dbName = bundle.getString("SELECT_DB");
        double iSelectLat = bundle.getDouble("SELECT_LAT");
        double iSelectLon = bundle.getDouble("SELECT_LON");
        double iDotCount = bundle.getDouble("SELECT_DOT_COUNT");


        if(iDotCount == 1) {
            LatLng curLoc = new LatLng(iSelectLat, iSelectLon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 15.0f));
            mMap.addMarker(new MarkerOptions().position(curLoc));
        }
        else{
            //LatLng curLoc = new LatLng(25.0336110, 121.5650000);

            PolylineOptions polylineOpt = new PolylineOptions()
                                                .width(15)
                                                .color(R.color.material_blue_grey_800);

            DBGetData repo = new DBGetData(this);
            ArrayList<LatLng> listLatLng = repo.getPathLatLngList(iSelectPath, dbName);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(listLatLng.size()/2), 15.0f));

            /* for debug, testing data
                listLatLng.add(new LatLng(25.032,121.5670000));
                listLatLng.add(new LatLng(25.0336110,121.5650000));
                listLatLng.add(new LatLng(25.037,121.5650000));
                listLatLng.add(new LatLng(25.038, 121.5630000));
            */
            for(LatLng eachPoint:listLatLng)
                mMap.addMarker(new MarkerOptions()
                        .position(eachPoint)
                        .alpha(0.7f)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            polylineOpt.addAll(listLatLng);
            mMap.addPolyline(polylineOpt);
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.map_info, null);
                TextView txtTitle = (TextView) v.findViewById(R.id.textView1);
                txtTitle.setText(iSelectName);
                TextView txtSnippet = (TextView) v.findViewById(R.id.textView2);
                txtSnippet.setText(iSelectId + "\n" + iSelectTime + "\n" + iSelectOwner);
                return v;
            }

            @Override
            public View getInfoContents(Marker marker) {
                marker.hideInfoWindow();
                return null;
            }
        });
    }
}
