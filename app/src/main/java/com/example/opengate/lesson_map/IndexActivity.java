package com.example.opengate.lesson_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class IndexActivity extends AppCompatActivity {

    private ImageButton btn_index_list;
    private ImageButton btn_index_pin;
    private ImageButton btn_index_gps;
    private ImageButton btn_index_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        btn_index_list = (ImageButton) findViewById(R.id.index_list);
        btn_index_list.setOnClickListener(btnBackClick);

        btn_index_pin = (ImageButton) findViewById(R.id.index_pin);
        btn_index_pin.setOnClickListener(btnBackClick2);

        btn_index_gps = (ImageButton) findViewById(R.id.index_gps);
        btn_index_gps.setOnClickListener(btnBackClick3);

        btn_index_search = (ImageButton) findViewById(R.id.index_search);
        btn_index_search.setOnClickListener(btnBackClick4);
    }

    private View.OnClickListener btnBackClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent it = new Intent();
            it.setClass(IndexActivity.this, MenuActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("SELECT_DB", ItemGPS.DATABASE_TABLE);
            it.putExtras(bundle);

            startActivity(it);
        }
    };

    private View.OnClickListener btnBackClick2 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent it = new Intent();
            it.setClass(IndexActivity.this, GeoCoderActivity.class);
            startActivity(it);
        }
    };
    private View.OnClickListener btnBackClick3 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent it = new Intent();
            it.setClass(IndexActivity.this, GPS.class);
            startActivity(it);
        }
    };

    private View.OnClickListener btnBackClick4 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent it = new Intent();
            it.setClass(IndexActivity.this, SearchActivity.class);
            startActivity(it);
        }
    };
}
