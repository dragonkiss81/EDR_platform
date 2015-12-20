package com.example.opengate.lesson_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.view.View;

public class ViewList extends AppCompatActivity {

    /* declaration */
    private Button btn_watch;
    private Spinner spn_loc;
    private String ms_loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        /* variable initialization */
        btn_watch = (Button) findViewById(R.id.btn_watch);
        spn_loc = (Spinner) findViewById(R.id.spn_loc);
        spn_loc.setOnItemSelectedListener(spnSexOnItemSelected);
        btn_watch.setOnClickListener(btnOKonClick);
    }

    /* for spinner check */
    private AdapterView.OnItemSelectedListener spnSexOnItemSelected = new AdapterView.OnItemSelectedListener(){

        public void onItemSelected(AdapterView parent, View v, int position, long id){
            ms_loc = parent.getSelectedItem().toString();
        }
        public  void onNothingSelected(AdapterView parent){
        }
    };

    /* Intent when click the btn (using bundle) */
    private View.OnClickListener btnOKonClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent it = new Intent();
            it.setClass(ViewList.this, MapsActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("SELECT_LOC",ms_loc);

            if(ms_loc.equals(getString(R.string.loc_taipei))) {
                bundle.putDouble("SELECT_LAT", 25.0336110);
                bundle.putDouble("SELECT_LON", 121.5650000);
            }
            else if(ms_loc.equals(getString(R.string.loc_taichung))) {
                bundle.putDouble("SELECT_LAT", 24.1477360);
                bundle.putDouble("SELECT_LON", 120.6736480);
            }
            else if(ms_loc.equals(getString(R.string.loc_tainan))){
                bundle.putDouble("SELECT_LAT", 22.9999000);
                bundle.putDouble("SELECT_LON", 120.2268760);
            }
            else{
                bundle.putDouble("SELECT_LAT", 22.6272780);
                bundle.putDouble("SELECT_LON", 120.3014350);
            }

            it.putExtras(bundle);
            startActivity(it);
        }
    };
}
