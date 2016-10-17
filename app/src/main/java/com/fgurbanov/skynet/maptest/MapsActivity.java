package com.fgurbanov.skynet.maptest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

import com.fgurbanov.skynet.maptest.data.TrackData;
import com.fgurbanov.skynet.maptest.data.TrackPoints;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static String LOG_TAG = "my_log";
    public final static String EXTRA_MESSAGE = "TrackData";
    public final static String JSON_URL_RES = "http://avionicus.com/android/track_v0649.php?avkey=1M1TE9oeWTDK6gFME9JYWXqpAGc%3D&hash=58ecdea2a91f32aa4c9a1d2ea010adcf2348166a04&track_id=36131&user_id=22";
    public final static double Trans_Color_Const = 12.75;
    private GoogleMap mMap;

    ProgressDialog PD;

    //data
    private TrackData aTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // parse JSON object
        PD = new ProgressDialog(this);
        PD.setMessage("Preparing your track.....");
        PD.setCancelable(false);

        //parse JSON Object
        //parseJsonObject(loadJSONFromAsset());

        // Connected to server, load JSON, and parse
        new ParseTask().execute();

        //add mapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //Set button for switch map activity
        Button changeMapButton = (Button) findViewById(R.id.changeMapToOSM);
        changeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, OSMActivity.class);
                intent.putExtra(EXTRA_MESSAGE, aTrack);
                startActivity(intent);
            }
        });
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings myUiSettings = mMap.getUiSettings();
        myUiSettings.setZoomControlsEnabled(true);

        try {
            LatLng startTrack = aTrack.getTrackPointses().get(0).getCoordinates();
            //set marker of beginning of track
            mMap.addMarker(new MarkerOptions().position(startTrack).title("Marker in Start"));

            //create gradient line on Map
            // this line is sum of mini line
            int size = aTrack.getTrackPointses().size();
            for(int i = 0; i < size-1; i++ ){

                LatLng midpoint = new LatLng(
                        (aTrack.getTrackPointses().get(i).getCoordinates().latitude + (aTrack.getTrackPointses().get(i+1).getCoordinates()).latitude)/2,
                        (aTrack.getTrackPointses().get(i).getCoordinates().longitude + (aTrack.getTrackPointses().get(i+1).getCoordinates()).longitude)/2

                );
                PolylineOptions polylineOptions1 = new PolylineOptions()
                        .add((aTrack.getTrackPointses().get(i).getCoordinates()))
                        .add(midpoint)
                        .geodesic(true)
                        .color(aTrack.getColor(i))
                        .width(12);
                PolylineOptions polylineOptions2 = new PolylineOptions()
                        .add(midpoint)
                        .add(aTrack.getTrackPointses().get(i+1).getCoordinates())
                        .geodesic(true)
                        .color(aTrack.getColor(i+1))
                        .width(12);
                mMap.addPolyline(polylineOptions1);
                mMap.addPolyline(polylineOptions2);
            }

            // set marker of  end track
            LatLng endTrack = aTrack.getTrackPointses().get(size-1).getCoordinates();
            mMap.addMarker(new MarkerOptions().position(endTrack).title("Marker in End"));

            // set camera at the beginning of track
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startTrack, 15));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJsonObject(String strJson) {
        PD.show();
        try {
            JSONObject  jsonRootObject = new JSONObject(strJson);

            // read aWaypoints
            JSONArray jsonWaypointsObject = jsonRootObject.getJSONArray("aWaypoints");

            // read aTrack
            JSONObject jsonTrackObject = jsonRootObject.getJSONObject("aTrack");

            //create aTrack object
            String sTemp = jsonTrackObject.optString("type");
            String dtStart = jsonTrackObject.optString("dt_start");
            String dtEnd = jsonTrackObject.optString("dt_end");
            aTrack = new TrackData(sTemp, dtStart, dtEnd);

            // read aPoints
            // Speed, coordinates and etc.
            JSONArray jsonPointsArray = jsonRootObject.getJSONArray("aPoints");
            ArrayList<TrackPoints> pointsList = new ArrayList<>();
            for( int i = 0; i <jsonPointsArray.length(); i++){
                JSONArray jsonPointArray = jsonPointsArray.getJSONArray(i);
                ///////////////
                double latitude = jsonPointArray.getDouble(0);
                double longitude= jsonPointArray.getDouble(1);
                LatLng coordinates = new LatLng(latitude,longitude);
                double altitude = jsonPointArray.getDouble(2);
                long timeStamp = jsonPointArray.getLong(3);
                double heartRate = jsonPointArray.getDouble(4);
                double speed = jsonPointArray.getDouble(5);
                int direction = jsonPointArray.getInt(6);
                /////////////
                TrackPoints pointElement = new TrackPoints(coordinates, altitude, timeStamp,
                        heartRate, speed, direction);
                pointsList.add(pointElement);
            }
            aTrack.setTrackPointses(pointsList);
            PD.dismiss();
            Log.d(LOG_TAG, "I am poarse smth");
            Log.d(LOG_TAG, aTrack.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            PD.dismiss();
            Log.d(LOG_TAG, "ALLERT MTFKER");
        }
        onMapReady(mMap);
    }


    public String loadJSONFromAsset() {
        //String json = null;
        String json;
        try {
            InputStream is = getAssets().open("track_v0649.json");
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            //PD.show();
            // получаем данные с внешнего ресурса
            try {
                //create URL to Server
                URL url = new URL(JSON_URL_RES);

                //create connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //generate data input stream
                InputStream inputStream = urlConnection.getInputStream();

                StringBuilder buffer = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            Log.d(LOG_TAG, strJson);
            try {
                parseJsonObject(strJson);
            } catch (Exception e){
                Log.d(LOG_TAG, "not parse");
            }
        }
    }


}
