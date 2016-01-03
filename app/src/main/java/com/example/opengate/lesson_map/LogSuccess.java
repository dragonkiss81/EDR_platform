package com.example.opengate.lesson_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LogSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_success);

        Intent intent = new Intent();
        intent.setClass(LogSuccess.this, GPS.class);
        startActivity(intent);
    }

}
