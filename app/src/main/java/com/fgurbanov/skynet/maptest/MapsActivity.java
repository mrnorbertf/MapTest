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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public final static String EXTRA_MESSAGE = "TrackData";
    public final static double Trans_Color_Const = 12.75;
    private GoogleMap mMap;

    ProgressDialog PD;

    //data
    private TrackData aTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //add mapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // parse JSON object
        PD = new ProgressDialog(this);
        PD.setMessage("Preparing your track.....");
        PD.setCancelable(false);

        //parse JSON Object
        parseJsonObject(loadJSONFromAsset());

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
                PolylineOptions polylineOptions = new PolylineOptions()
                        .add((aTrack.getTrackPointses().get(i).getCoordinates()))
                        .add(aTrack.getTrackPointses().get(i+1).getCoordinates())
                        .geodesic(true)
                        .color(aTrack.getColor(i));

                mMap.addPolyline(polylineOptions);
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
