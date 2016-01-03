package com.example.opengate.lesson_map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class DBGetData{
    
    private DBService dbService;
    public DBGetData(Context context) {
        dbService = new DBService(context);
    }

    public int insert(ItemGPS ItemGPS, String dbName) {

        SQLiteDatabase db = dbService.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemGPS.KEY_SR, ItemGPS.sr);
        values.put(ItemGPS.KEY_ID, ItemGPS.uid);
        values.put(ItemGPS.KEY_PATH, ItemGPS.path);
        values.put(ItemGPS.KEY_NAME, ItemGPS.name);
        values.put(ItemGPS.KEY_LAT, ItemGPS.lat);
        values.put(ItemGPS.KEY_LON, ItemGPS.lon);
        values.put(ItemGPS.KEY_TIME, ItemGPS.time);
        values.put(ItemGPS.KEY_OWNER, ItemGPS.owner);
        values.put(ItemGPS.KEY_INFO, ItemGPS.info);

        // Inserting Row
        long ItemGPS_Id = db.insert(dbName, null, values);
        db.close(); // Closing database connection
        return (int) ItemGPS_Id;
    }

    public ArrayList<HashMap<String, Object>> getAll(String dbName) {
        SQLiteDatabase db = dbService.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                ItemGPS.KEY_SR + "," +
                ItemGPS.KEY_ID + "," +
                ItemGPS.KEY_PATH + "," +
                ItemGPS.KEY_NAME + "," +
                ItemGPS.KEY_LAT + "," +
                ItemGPS.KEY_LON + "," +
                ItemGPS.KEY_TIME + "," +
                ItemGPS.KEY_OWNER + "," +
                ItemGPS.KEY_INFO +  "," +
                "count(" + ItemGPS.KEY_PATH + ") as dot_count" +
                " FROM " +  dbName +
                " GROUP BY " + ItemGPS.KEY_PATH ;

        ArrayList<HashMap<String, Object>> ItemGPSList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> checkpoint = new HashMap<>();
                checkpoint.put("name", cursor.getString(cursor.getColumnIndex(ItemGPS.KEY_NAME)));
                checkpoint.put("id",cursor.getString(cursor.getColumnIndex(ItemGPS.KEY_ID)));
                checkpoint.put("path",cursor.getString(cursor.getColumnIndex(ItemGPS.KEY_PATH)));
                checkpoint.put("time",cursor.getString(cursor.getColumnIndex(ItemGPS.KEY_TIME)));
                checkpoint.put("owner",cursor.getString(cursor.getColumnIndex(ItemGPS.KEY_OWNER)));
                checkpoint.put("lan", cursor.getString(cursor.getColumnIndex(ItemGPS.KEY_LAT)));
                checkpoint.put("lon", cursor.getString(cursor.getColumnIndex(ItemGPS.KEY_LON)));
                checkpoint.put("dot_count", cursor.getString(cursor.getColumnIndex("dot_count")));
                ItemGPSList.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ItemGPSList;
    }



    public void rawQuery(String query) {
        SQLiteDatabase db = dbService.getReadableDatabase();
        String selectQuery =  query;

        db.execSQL(selectQuery);
        //Cursor cursor = db.rawQuery(selectQuery, null);
        //cursor.close();

        db.close();
        return;
    }

    public int getCount(String dbName) {
        int result = 0;
        SQLiteDatabase db = dbService.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + dbName, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public ArrayList<LatLng> getPathLatLngList(String curPath, String dbName){

        ArrayList<LatLng> listLatLng = new ArrayList<>();

        SQLiteDatabase db = dbService.getReadableDatabase();

        String selectQuery =  "SELECT  " +
                ItemGPS.KEY_LAT + "," +
                ItemGPS.KEY_LON +
                " FROM " + dbName +
                " WHERE " + ItemGPS.KEY_PATH + " = " + "\"" + curPath+ "\"";
        // example : String selectQuery =  "SELECT LAT,LON FROM GPS_TABLE WHERE PATH = \"001\"";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                LatLng checkpoint = new LatLng(Double.valueOf(cursor.getString(cursor.getColumnIndex(ItemGPS.KEY_LAT))),
                                           Double.valueOf(cursor.getString(cursor.getColumnIndex(ItemGPS.KEY_LON))));
                listLatLng.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listLatLng;
    }

    public void sample(String dbName) {

        if (getCount(dbName) == 0) {

        ItemGPS item = new ItemGPS();
        ItemGPS item2 = new ItemGPS();
        ItemGPS item3 = new ItemGPS();

        item.sr = (int) (Math.random()*100000+1);
        item.uid = "AC0001";
        item.path = String.valueOf(item.sr);
        item.name = "Rear-end collision in Taipei";
        item.lat = 25.032;
        item.lon = 121.5670000;
        item.time = "2015/01/01";
        item.owner = "John";
        item.info = "WTH!!!";
        insert(item, dbName);

        item.sr = item.sr+1;
        item.lat =  25.0336110;
        item.lon =  121.5650000;
        insert(item, dbName);

        item.sr = item.sr+1;
        item.lat =  25.037;
        item.lon = 121.5650000;
        insert(item, dbName);

        item.sr = item.sr+1;
        item.lat =  25.038;
        item.lon = 121.5630000;
        insert(item, dbName);

        item2.sr = (int) (Math.random()*100000+1);
        item2.uid = "AC0002";
        item2.path = String.valueOf(item2.sr);
        item2.name = "Hit and run  in Taichung";
        item2.lat = 24.1477360;
        item2.lon = 120.6736480;
        item2.time = "2015/01/02";
        item2.owner = "Amy";
        item2.info = "amazing~~";
        insert(item2, dbName);

        item3.sr = (int) (Math.random()*100000+1);
        item3.uid = "AC0003";
        item3.path = String.valueOf(item3.sr);
        item3.name = "Car crash in Tainan";
        item3.lat =  22.9999000;
        item3.lon =  120.2268760;
        item3.time = "2015/01/03";
        item3.owner = "Tom";
        item3.info = "quite simple :)";
        insert(item3, dbName);

        }
    }
}
