package com.example.opengate.lesson_map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBService  extends SQLiteOpenHelper {

    //version number to upgrade database version
    private static final int DATABASE_VERSION = 4;
    // Database Name
    private static final String DATABASE_NAME = "trackSys.db";

    public DBService(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + ItemGPS.DATABASE_TABLE  + "("
                + ItemGPS.KEY_SR  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + ItemGPS.KEY_ID + " TEXT, "
                + ItemGPS.KEY_PATH + " TEXT, "
                + ItemGPS.KEY_NAME + " TEXT, "
                + ItemGPS.KEY_LAT + " REAL, "
                + ItemGPS.KEY_LON + " REAL, "
                + ItemGPS.KEY_TIME + " TEXT, "
                + ItemGPS.KEY_OWNER + " TEXT, "
                + ItemGPS.KEY_INFO + " TEXT )";

        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE_2 = "CREATE TABLE " + ItemGPS.DATABASE_TABLE_2  + "("
                + ItemGPS.KEY_SR  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + ItemGPS.KEY_ID + " TEXT, "
                + ItemGPS.KEY_PATH + " TEXT, "
                + ItemGPS.KEY_NAME + " TEXT, "
                + ItemGPS.KEY_LAT + " REAL, "
                + ItemGPS.KEY_LON + " REAL, "
                + ItemGPS.KEY_TIME + " TEXT, "
                + ItemGPS.KEY_OWNER + " TEXT, "
                + ItemGPS.KEY_INFO + " TEXT )";

        db.execSQL(CREATE_TABLE_2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + ItemGPS.DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ItemGPS.DATABASE_TABLE_2);
        // Create tables again
        onCreate(db);
    }
}
