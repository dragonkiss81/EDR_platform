package com.example.opengate.lesson_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {

    ArrayList<HashMap<String, Object>> locationList;
    private SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ListView lv = (ListView)findViewById(R.id.listView);

        Bundle bundle = getIntent().getExtras();
        final String dbName = bundle.getString("SELECT_DB");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent();
                it.setClass(MenuActivity.this, MapsActivity.class);
                ListView listView = (ListView) parent;
                HashMap<String, Object> data = (HashMap<String, Object>) listView.getItemAtPosition(position);

                String ms_name = data.get("name").toString();
                String ms_id = data.get("id").toString();
                String ms_path = data.get("path").toString();
                String ms_time = data.get("time").toString();
                String ms_owner = data.get("owner").toString();
                String ms_lan = data.get("lan").toString();
                String ms_lon = data.get("lon").toString();
                String ms_dot_count = data.get("dot_count").toString();

                Bundle bundle = new Bundle();
                bundle.putString("SELECT_NAME", ms_name);
                bundle.putString("SELECT_ID", ms_id);
                bundle.putString("SELECT_PATH", ms_path);
                bundle.putString("SELECT_TIME", ms_time);
                bundle.putString("SELECT_OWNER", ms_owner);
                bundle.putString("SELECT_DB", dbName);
                bundle.putDouble("SELECT_LAT", Double.parseDouble(ms_lan));
                bundle.putDouble("SELECT_LON", Double.parseDouble(ms_lon));
                bundle.putDouble("SELECT_DOT_COUNT", Double.parseDouble(ms_dot_count));

                it.putExtras(bundle);
                startActivity(it);
            }
        });

        //for Database initial
        DBGetData repo = new DBGetData(this);
        repo.sample(dbName);

        //query the data
        locationList =  repo.getAll(dbName);
        for( HashMap<String, Object> entry:locationList)
        {
            if( Integer.parseInt( (String) entry.get("dot_count") ) == 1)
                entry.put("dot_type", R.mipmap.viewlist_marker);
            else
                entry.put("dot_type", R.mipmap.viewlist_waypoint);
        }

        //set to view front-end (using adapter)
        adapter = new SimpleAdapter(
                this,
                locationList,
                R.layout.menulistview,
                new String[] { "name","id" ,"time" ,"owner","dot_type" },
                new int[]{R.id.textView1, R.id.textView2 , R.id.textView3 , R.id.textView4, R.id.listImageView } );
        lv.setAdapter(adapter);
    }
}





