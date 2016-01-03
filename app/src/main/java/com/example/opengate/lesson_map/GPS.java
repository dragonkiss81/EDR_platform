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
    private int TM_Interval = 1000 * 10;    // 憭?摮?銝甈??桐?ms
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

        /*
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        */

        locationManager.removeUpdates(locationListener);
        locationManager.removeNmeaListener(nmeaListener);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //	return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    private void registerListener(){
        locationListener=new LocationListener(){

            @Override
            public void onLocationChanged(Location loc) {
                // TODO Auto-generated method stub
                //摰?鞈??湔???
                Log.d("GPS-NMEA", loc.getLatitude() + "," +  loc.getLongitude());
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                //摰????????????潘?銝血?????靘?逍rovider摮葡銝?
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                //摰????????????潘?銝血?????靘?逍rovider摮葡銝?
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
                Log.d("GPS-NMEA", provider + "");
                //GPS???靘????靘gps????雿?
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
        String filename = "GPS_OP_Count.txte";
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
                // Todo ?甈⊥(time)
                if (Lang != "" && Long != "" && cboxGPS_Enabled.isChecked()) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis()) ;
                    String strDT = formatter.format(curDate);

                    if ((curDate.getTime() - PriorDate.getTime()) > TM_Interval) {
                        Log.d("GPS-NMEA", "GPS_DATA_SAVE");
                        SyncObj.uploadForGPS(
                                "DATENTIME", strDT,
                                "LAT", Lang,
                                "LONGI", Long);
                        txtTimes.setText("Time:" + strDT);
                        txtSend.setText("Send:" + TimesCount);
                        TimesCount++;
                        PriorDate = curDate;

                    }
                }
            }
        };
    }

    //??nmea鞈??allback
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
        //NMEA ??敺checksum number
        byte checksumCalcValue = 0;
        int checksumValue;

        //瑼Ｘ??臬??
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
