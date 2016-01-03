/**
 * Created by DanielLin on 2015/12/27.
 */
package com.example.opengate.lesson_map;
/**
 * Created by DanielLin on 2015/12/27.
 */
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Synchronizer {
    private static final String SERVER_URL="http://23.102.179.154/index.py";

    private static final String SERVER_SEARCH_URL="http://23.102.179.154/search.py";

    private static final String TAG="Synchronizer";

    private static HttpClient clientForGPS;

    private static HttpGet getForGPS;

    private static HttpResponse responseForGPS;

    private static HttpEntity resEntityForGPS;

    private static HttpClient clientForPin;

    private static HttpGet getForPin;

    private static HttpResponse responseForPin;

    private static HttpEntity resEntityForPin;

    private static HttpClient clientForSearch;

    private static HttpGet getForSearch;

    private static HttpResponse responseForSearch;

    private static HttpEntity resEntityForSearch;

    public void uploadForGPS(String timeName,String timeData,String lanName,String lanData,String longName,String longData){
        String targetURL;

        targetURL = SERVER_URL + "?" + "UID" + "=" + "xxx" + "&" + timeName + "=" + timeData.replaceAll(" ","%20") + "&" + "URL" + "=" + "xxx" + "&" + "NAME" + "=" + "xxx" + "&" +lanName + "=" +lanData + "&" +longName + "=" +longData+ "&" +"ADDRESS"+ "=" +"xxx" + "&" + "PATH" + "=" + "00001";
        clientForGPS = new DefaultHttpClient();
        getForGPS = new HttpGet(targetURL);

        Thread thread = new Thread(){
            public void run(){
                try {
                    responseForGPS = clientForGPS.execute(getForGPS);
                    resEntityForGPS = responseForGPS.getEntity();
                    Log.i(TAG, EntityUtils.toString(resEntityForGPS));
                }
                catch (Exception e) {
                    Log.i(TAG,e.toString());
                }
            }
        };
        thread.start();
    }

    public void uploadForPinner(String timeName,String timeData,String URLName,String URLData,String nameName,String nameData,String lanName,String lanData,String longName,String longData,String addrName,String addrData){
        String targetURL;
        targetURL = SERVER_URL + "?" + "UID" + "=" + "xxx" + "&" + timeName + "=" + timeData.replaceAll(" ","%20") + "&" + URLName + "=" + URLData.replaceAll(" ","%20") + "&" +nameName + "=" + nameData.replaceAll(" ","%20") + "&" +lanName + "=" +lanData + "&" +longName + "=" +longData+ "&" +addrName+ "=" +addrData.replaceAll(" ","%20") + "&" + "PATH" + "=" + "1";
        clientForPin = new DefaultHttpClient();
        getForPin = new HttpGet(targetURL);

        Thread thread = new Thread(){
            public void run(){
                try {
                    responseForPin = clientForPin.execute(getForPin);
                    resEntityForPin = responseForPin.getEntity();
                    Log.i(TAG, EntityUtils.toString(resEntityForPin));
                }
                catch (Exception e) {
                    Log.i(TAG,e.toString());
                }
            }
        };
        thread.start();
    }

    public void downloadForSearch(String lanName,String lanData,String longName,String longData,String timeName,String timeData, final SearchCallback callback){
        String targetURL;
        targetURL = SERVER_SEARCH_URL + "?" + lanName + "=" + lanData + "&" + longName + "=" + longData + "&" + timeName + "=" + timeData.replaceAll(" ","%20");
        clientForSearch = new DefaultHttpClient();
        getForSearch = new HttpGet(targetURL);

        Thread thread = new Thread(){
            public void run(){
                try {
                    responseForSearch = clientForSearch.execute(getForSearch);
                    resEntityForSearch = responseForSearch.getEntity();
                    Log.i(TAG, EntityUtils.toString(resEntityForSearch));
                    callback.onResults(EntityUtils.toString(resEntityForSearch));
                }
                catch (Exception e) {
                    Log.i(TAG,e.toString());
                }
            }
        };
        thread.start();
    }
}
