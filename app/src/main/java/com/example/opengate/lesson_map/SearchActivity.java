package com.example.opengate.lesson_map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final static String TAG = "GeoCoderActivity";
    private GoogleMap map;
    private GoogleApiClient client;
    LatLng position;
    Address address;

    private Double LocationLat = 0.0;
    private Double LocationLong = 0.0;
    private String LocationAddress = "";


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initMap();

        LatLng curLoc = new LatLng(25.0336110, 121.5650000);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 15.0f));
        Marker marker =  map.addMarker(new MarkerOptions().position(curLoc));
        marker.setVisible(true);
    }

    private void initMap() {
        if (map == null) {
            map = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fmMap)).getMap();
            if (map != null) {
                //setUpMap();

                MyMarkerListener myMarkerListener = new MyMarkerListener();
                map.setOnMarkerDragListener(myMarkerListener);
            }
        }
    }

    private class MyMarkerListener implements GoogleMap.OnMarkerDragListener {

        @Override
        public void onMarkerDragStart(Marker marker) {

            Toast.makeText(getApplicationContext(), "onMarkerDragStart", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {

            map.clear();

            //得到最新Marker 的經緯度
            position = marker.getPosition();
            Geocoder geocoder = new Geocoder(SearchActivity.this);

            List<Address> addressList = null;
            int maxResults = 1;
            try {
                addressList = geocoder.getFromLocation(position.latitude,position.longitude, maxResults);
                address = addressList.get(0);

                String snippet = address.getAddressLine(0);


                LocationLat = position.latitude;

                LocationLong = position.longitude;
                LocationAddress = snippet;
                EditText etvideoTagInfo = (EditText) findViewById(R.id.etvideoTagInfo);
                etvideoTagInfo.setText(snippet);

                //打標記
                map.addMarker(new MarkerOptions().position(position)
                        .title("MyNewPIN").snippet(snippet).draggable(true));

                //設定拉地圖鏡頭焦點position的精準度，越大越清楚
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(position).zoom(17).build();


                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                Toast.makeText(getApplicationContext(), marker.getTitle() + " : " +snippet, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }

        }

        @Override
        public void onMarkerDrag(Marker marker) {

            //Toast.makeText(getApplicationContext(),"onMarkerDragEnd" + marker.getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMapReady() {
        if (map == null) {
            showToast(R.string.msg_MapNotReady);
            return false;
        }
        return true;
    }

    public void onLocationNameClick(View view) {
        if (!isMapReady()) {
            return;
        }

        EditText etLocationName = (EditText) findViewById(R.id.etLocationName);
        String locationName = etLocationName.getText().toString().trim();
        if (locationName.length() > 0) {
            //將user輸入的Location轉為Laptitute和Longtitute
            locationNameToMarker(locationName);
        } else {
            showToast(R.string.msg_LocationNameIsEmpty);
        }
    }

    private void locationNameToMarker(String locationName) {

        //先清除之前的Marker
        map.clear();
        Geocoder geocoder = new Geocoder(SearchActivity.this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            addressList = geocoder.getFromLocationName(locationName, maxResults);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            showToast(R.string.msg_LocationNameNotFound);
        } else {
            Address address = addressList.get(0);

            position = new LatLng(address.getLatitude(),
                    address.getLongitude());

            String snippet = address.getAddressLine(0);


            LocationLat = position.latitude;
            LocationLong = position.longitude;
            LocationAddress = snippet;
            TextView etvideoTagInfo = (TextView) findViewById(R.id.etvideoTagInfo);
            etvideoTagInfo.setText(snippet);

            //打標記
            map.addMarker(new MarkerOptions().position(position)
                    .title(locationName).snippet(snippet).draggable(true));

            //設定拉地圖鏡頭焦點position的精準度，越大越清楚
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(17).build();

            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMap();
    }

    private void showToast(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    public void onPinClick(View view) {

        EditText etvideoDatetime = (EditText) findViewById(R.id.etvideoDatetime);
        TextView etvideoTagInfo = (TextView) findViewById(R.id.etvideoTagInfo);
        final DBGetData repo = new DBGetData(this);

        //String videoDatetime = etvideoDatetime.getText().toString().trim();
        if (etvideoTagInfo.length() == 0) {
            showToast(R.string.msg_videoTagInfoIsEmpty);
        }
        else if (etvideoDatetime.length() == 0) {
            showToast(R.string.msg_videoDatetimeIsEmpty);
        }
        else
        {
            showToast(R.string.msg_OK);


            //Intent it = new Intent();
            //it.setClass(SearchActivity.this, MenuActivity.class);



            Synchronizer searchSync = new Synchronizer();
            searchSync.downloadForSearch("LAT", Double.toString(LocationLat), "LONGI", Double.toString(LocationLong), "DATENTIME", etvideoDatetime.getText().toString(), new SearchCallback() {
                public void onResults(String str) {
                    Log.i("SEARCH_CALLBACK!", "CALLBACK!!");


                    //Toast.makeText(SearchActivity.this, str, Toast.LENGTH_LONG).show();

                    str = "INSERT INTO SEARCH_TABLE (SR, UID, PATH, NAME, LAT, LON, TIME, OWNER, INFO) VALUES ( NULL, 0, 0, \"car accident\", 3.1555, 3.14, \"2015-11-11 12:00:10\", \"0\", \"jk\")";

                    Log.w("OD","LONG string : " + str);

                    String delims = "\n";
                    String[] tokens = str.split(delims);
                    for(String eachToken:tokens){
                        repo.rawQuery(eachToken);
                        Log.w("OD","eachtoken : " + eachToken);
                    }



                    Intent it = new Intent();
                    it.setClass(SearchActivity.this, MenuActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("SELECT_DB", ItemGPS.DATABASE_TABLE_2);
                    it.putExtras(bundle);

                    startActivity(it);
                }

            });
        }
    }
}

