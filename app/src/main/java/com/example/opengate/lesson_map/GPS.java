package com.example.opengate.lesson_map;

/**
 * Created by Fred  on 2015/12/25.
 */

import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.Profile;

import org.apache.http.util.EncodingUtils;

import java.text.SimpleDateFormat;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;

public class GPS extends Activity {
    private LocationManager locationManager = null;
    private Criteria criteria = null;
    private LocationListener locationListener = null;
    private GpsStatus.NmeaListener nmeaListener = null;
    private GpsStatus.Listener gpsStatusListener = null;
    private TextView txtGPS_Quality = null;
    private TextView txtGPS_Location = null;
    private TextView txtGPS_Satellites = null;
    private TextView txtTimes = null;
    private TextView txtSend = null;
    private CheckBox cboxGPS_Enabled = null;
    private Synchronizer SyncObj = null;
    private Boolean Enabled = false;
    private int GPS_OP_Count = 0;
    private int TimesCount = 0;
    private Date PriorDate = null;
    private int TM_Interval = 1000 * 10;
    //
    private Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        //
        Log.i("1","111");
        txtGPS_Quality = (TextView) findViewById(R.id.textGPS_Quality);
        txtGPS_Location = (TextView) findViewById(R.id.textGPS_Location);
        txtGPS_Satellites = (TextView) findViewById(R.id.textGPS_Satellites);
        txtTimes = (TextView) findViewById(R.id.textTimes);
        txtSend = (TextView) findViewById(R.id.textSend);
        SyncObj = new Synchronizer();
        cboxGPS_Enabled = (CheckBox) findViewById(R.id.cboxGPS_Enabled);
        registerHandler();
        registerListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        locationManager.addNmeaListener(nmeaListener);
        //
        //
        cboxGPS_Enabled.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (cboxGPS_Enabled.isChecked()) {
                    if (!Enabled) {
                        GPS_OP_Count = GetOperationCount();
                    }
                }
                Enabled = cboxGPS_Enabled.isChecked();
            }
        });
        PriorDate = new Date(System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        locationManager.removeUpdates(locationListener);
        locationManager.removeNmeaListener(nmeaListener);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void registerListener(){
        locationListener=new LocationListener(){

            @Override
            public void onLocationChanged(Location loc) {
                // TODO Auto-generated method stub
                Log.d("GPS-NMEA", loc.getLatitude() + "," +  loc.getLongitude());
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
                Log.d("GPS-NMEA", provider + "");

                switch (status) {
                    case LocationProvider.OUT_OF_SERVICE:
                        Log.d("GPS-NMEA","OUT_OF_SERVICE");
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Log.d("GPS-NMEA"," TEMPORARILY_UNAVAILABLE");
                        break;
                    case LocationProvider.AVAILABLE:
                        Log.d("GPS-NMEA","" + provider + "");
                        break;
                }
            }
        };

        nmeaListener = new GpsStatus.NmeaListener() {
            public void onNmeaReceived(long timestamp, String nmea) {
                //check nmea's checksum
                if (isValidForNmea(nmea)){
                    nmeaProgress(nmea);
                    Log.d("GPS-NMEA", nmea);
                }
            }
        };
//
        gpsStatusListener = new GpsStatus.Listener(){
            public void onGpsStatusChanged(int event) {
                // TODO Auto-generated method stub
                GpsStatus gpsStatus;
                gpsStatus= locationManager.getGpsStatus(null);

                switch(event)
                {
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        //
                        gpsStatus.getTimeToFirstFix();
                        Log.d("GPS-NMEA","GPS_EVENT_FIRST_FIX");
                        break;
                    case GpsStatus.GPS_EVENT_SATELLITE_STATUS:

                        Iterable<GpsSatellite> allSatellites = gpsStatus.getSatellites();
                        Iterator<GpsSatellite> it=allSatellites.iterator();

                        int count = 0;
                        while(it.hasNext())
                        {
                            GpsSatellite gsl=(GpsSatellite)it.next();

                            if (gsl.getSnr()>0.0){
                                count++;
                            }
                        }
                        break;
                    case GpsStatus.GPS_EVENT_STARTED:
                        //Event sent when the GPS system has started.
                        Log.d("GPS-NMEA","GPS_EVENT_STARTED");
                        break;
                    case GpsStatus.GPS_EVENT_STOPPED:
                        //Event sent when the GPS system has stopped.
                        Log.d("GPS-NMEA","GPS_EVENT_STOPPED");
                        break;
                    default :
                        break;
                }
            }
        };

    }

    private int GetOperationCount()
    {
        String filename = "GPS_OP_Count.txt";
        int Count = 0;
        String result="";
        try {
            FileInputStream fin = openFileInput(filename);
            int lenght = fin.available();
            byte[] buffer = new byte[lenght];
            fin.read(buffer);
            result = EncodingUtils.getString(buffer,"UTF-8");
            Count = Integer.parseInt(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String string = Integer.toString(++ Count);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Count;
    }

    private void registerHandler(){
        final DBGetData repo = new DBGetData(this);
	/*
	GGA Global Positioning System Fix Data. Time, Position and fix related data for a GPS receiver
	11
	1 2 34 5678 910|121314 15
	||||||||||||||| $--GGA,hhmmss.ss,llll.ll,a,yyyyy.yy,a,x,xx,x.x,x.x,M,x.x,M,x.x,xxxx*hh
	1) Time (UTC)
	2) Latitude
	3) N or S (North or South)
	4) Longitude
	5) E or W (East or West)
	6) GPS Quality Indicator,
	0 - fix not available,
	1 - GPS fix,
	2 - Differential GPS fix
	7) Number of satellites in view, 00 - 12
	8) Horizontal Dilution of precision
	9) Antenna Altitude above/below mean-sea-level (geoid)
	10) Units of antenna altitude, meters
	11) Geoidal separation, the difference between the WGS-84 earth
	ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level below ellipsoid
	12) Units of geoidal separation, meters
	13) Age of differential GPS data, time in seconds since last SC104
	type 1 or 9 update, null field when DGPS is not used
	14) Differential reference station ID, 0000-1023
	15) Checksum
		 */
        mHandler = new Handler() {
            public void handleMessage(Message msg) {

                String str = (String) msg.obj;
                String[] rawNmeaSplit = str.split(",");
                txtGPS_Quality.setText(rawNmeaSplit[6]);
                String Lang = rawNmeaSplit[2];
                String Long = rawNmeaSplit[4];
                txtGPS_Location.setText(rawNmeaSplit[2] + " " + rawNmeaSplit[3] + "," + rawNmeaSplit[4] + " " + rawNmeaSplit[5]);
                txtGPS_Satellites.setText(rawNmeaSplit[7]);

                Log.d("GPS-NMEA", "GPS DATA is READY");
                if (Lang != "" && Long != "" && cboxGPS_Enabled.isChecked()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis()) ;
                    String strDT = formatter.format(curDate);

                    if ((curDate.getTime() - PriorDate.getTime()) > TM_Interval) {
                        Log.d("GPS-NMEA", "GPS_DATA_SAVE");
                        if( Long.length() > 0 && Lang.length() > 0) {
                            String lang_1 = Lang.substring(0, 2);
                            float lang_2 = Float.parseFloat(Lang.substring(2));
                            lang_2 = lang_2 / 60.0f;
                            Lang = lang_1 + Float.toString(lang_2).substring(1);

                            String long_1 = Long.substring(0, 3);
                            float long_2 = Float.parseFloat(Long.substring(3));
                            long_2 = long_2 / 60.0f;
                            Long = long_1 + Float.toString(long_2).substring(1);


                            SyncObj.uploadForGPS(
                                    "DATENTIME", strDT,
                                    "LAT", Lang,
                                    "LONGI", Long,
                                    "PATH", Integer.toString(GPS_OP_Count));
                            txtTimes.setText("Time::" + strDT);
                            txtSend.setText("Send:" + TimesCount + " ,OP times:" + GPS_OP_Count);
                            TimesCount++;
                            PriorDate = curDate;

                            Profile profile = Profile.getCurrentProfile();
                            String facebookID = profile.getId();
                            String facebook_name = profile.getName();

                            repo.rawQuery(" INSERT INTO" + ItemGPS.DATABASE_TABLE +
                                    "(SR, UID, PATH, NAME, LAT, LON, TIME, OWNER, INFO)VALUES(" +
                                    "NULL" + "," +
                                    facebookID + "," +
                                    Integer.toString(GPS_OP_Count)  + "," +
                                    "GPS record:" + Integer.toString(GPS_OP_Count) + "," +
                                    Lang + "," +
                                    Long + "," +
                                    strDT + "," +
                                    facebook_name +"," +
                                    "infomation");


                        }
                    }
                }
            }
        };
    }

    // Callback function for NMEA code process
    private void nmeaProgress(String rawNmea){

        String[] rawNmeaSplit = rawNmea.split(",");

        if (rawNmeaSplit[0].equalsIgnoreCase("$GPGGA")){
            //send GGA nmea data to handler
            Message msg = new Message();
            msg.obj = rawNmea;
            mHandler.sendMessage(msg);
        }
    }

    private boolean isValidForNmea(String rawNmea){
        boolean valid = true;
        byte[] bytes = rawNmea.getBytes();
        int checksumIndex = rawNmea.indexOf("*");
        byte checksumCalcValue = 0;
        int checksumValue;

        if ((rawNmea.charAt(0) != '$') || (checksumIndex==-1)){
            valid = false;
        }

        if (valid){
            String val = rawNmea.substring(checksumIndex + 1, rawNmea.length()).trim();
            checksumValue = Integer.parseInt(val, 16);
            for (int i = 1; i < checksumIndex; i++){
                checksumCalcValue = (byte) (checksumCalcValue ^ bytes[i]);
            }
            if (checksumValue != checksumCalcValue){
                valid = false;
            }
        }

        return valid;
    }
}
