package com.fgurbanov.skynet.maptest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public final static String EXTRA_MESSAGE = "TrackData";
    private GoogleMap mMap;

    ProgressDialog PD;

    //data
    private TrackData aTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // parse JSON object
        PD = new ProgressDialog(this);
        PD.setMessage("Preparing your track.....");
        PD.setCancelable(false);

        parseJsonObject(loadJSONFromAsset());

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

        LatLng startTrack = null;
        try {
            startTrack = aTrack.getTrackPointses().get(0).getCoordinates();

            mMap.addMarker(new MarkerOptions().position(startTrack).title("Marker in Start"));

            int size = aTrack.getTrackPointses().size();
            int speedFlag = 5;
            PolylineOptions rectOptions = new PolylineOptions();
            double speed = 0;

            for(int i = 0; i < size; i++ ){
                rectOptions.geodesic(true);
                speed = aTrack.getTrackPointses().get(i).getSpeed();
                if (speed < 5) {
                    if (speedFlag != 5) {
                        rectOptions.add(aTrack.getTrackPointses().get(i).getCoordinates());
                        mMap.addPolyline(rectOptions);
                        rectOptions = new PolylineOptions();
                    }
                    rectOptions.add(aTrack.getTrackPointses().get(i).getCoordinates()).color(Color.parseColor("#40E0D0"));
                    speedFlag = 5;
                } else if (speed < 10) {
                    if (speedFlag != 10) {
                        rectOptions.add(aTrack.getTrackPointses().get(i).getCoordinates());
                        mMap.addPolyline(rectOptions);
                        rectOptions = new PolylineOptions();
                    }
                    rectOptions.add(aTrack.getTrackPointses().get(i).getCoordinates()).color(Color.parseColor("#008000"));
                    speedFlag = 10;
                } else if (speed < 15){
                    if (speedFlag != 15) {
                        rectOptions.add(aTrack.getTrackPointses().get(i).getCoordinates());
                        mMap.addPolyline(rectOptions);
                        rectOptions = new PolylineOptions();
                    }

                    rectOptions.add(aTrack.getTrackPointses().get(i).getCoordinates()).color(Color.parseColor("#FFFF00"));
                    speedFlag = 15;
                    } else  {
                        if (speedFlag != 20) {
                            rectOptions.add(aTrack.getTrackPointses().get(i).getCoordinates());
                            mMap.addPolyline(rectOptions);
                            rectOptions = new PolylineOptions();
                        }
                    rectOptions.add(aTrack.getTrackPointses().get(i).getCoordinates()).color(Color.parseColor("#FF0000"));
                    speedFlag = 20;
                    }

            }
            LatLng endTrack = aTrack.getTrackPointses().get(size-1).getCoordinates();
            mMap.addMarker(new MarkerOptions().position(endTrack).title("Marker in End"));


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

        } catch (JSONException e) { e.printStackTrace(); PD.dismiss(); }
    }


    public String loadJSONFromAsset() {
        String json = null;
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

}
