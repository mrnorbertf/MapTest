package com.fgurbanov.skynet.maptest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fgurbanov.skynet.maptest.data.TrackData;
import com.google.android.gms.maps.model.PolylineOptions;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class OSMActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "TrackData";
    private TrackData aTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osm);

        org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);

        //create map
        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //add zoom
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //receive Track  data
        Intent intent = getIntent();
        aTrack = (TrackData)intent.getSerializableExtra(EXTRA_MESSAGE);

        Button changeMapButton = (Button) findViewById(R.id.changeMapToGoogle);
        changeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        prepareMap(map);

    }

    public void prepareMap(MapView map) {
        IMapController mapController = map.getController();

        // set Center
        mapController.setZoom(16);
        GeoPoint startPoint =   aTrack.getTrackPointses().get(0).getGeoPoint();
        int size =   aTrack.getTrackPointses().size();
        GeoPoint endPoint =   aTrack.getTrackPointses().get(size - 1).getGeoPoint();
        mapController.setCenter(startPoint);

        //marker end and start
        Marker startMarker = new Marker(map);
        Marker endMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        endMarker.setPosition(endPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        map.getOverlays().add(endMarker);
        startMarker.setTitle("Start point");
        endMarker.setTitle("End point");

        //create track
        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);


        Polyline roadOverlay = new Polyline();
        List<GeoPoint> points = new ArrayList<>();

        int speedFlag = 5;
        for(int i = 0; i < size; i++ ){

            roadOverlay.setGeodesic(true);
            double speed = aTrack.getTrackPointses().get(i).getSpeed();
            points.add(aTrack.getTrackPointses().get(i).getGeoPoint());
            if (speed < 5) {
                if (speedFlag != 5) {
                    roadOverlay.setPoints(points);
                    map.getOverlays().add(roadOverlay);
                    roadOverlay = new Polyline();
                    points = new ArrayList<>();
                }
                roadOverlay.setColor(getResources().getColor(R.color.colorSlow));
                speedFlag = 5;
            } else if (speed < 10) {
                if (speedFlag != 10) {
                    roadOverlay.setPoints(points);
                    map.getOverlays().add(roadOverlay);
                    roadOverlay = new Polyline();
                    points = new ArrayList<>();
                }
                roadOverlay.setColor(getResources().getColor(R.color.colorOptimum));
                speedFlag = 10;
            } else if (speed < 15){
                if (speedFlag != 15) {
                    roadOverlay.setPoints(points);
                    map.getOverlays().add(roadOverlay);
                    roadOverlay = new Polyline();
                    points = new ArrayList<>();
                }
                roadOverlay.setColor(getResources().getColor(R.color.colorFast));
                speedFlag = 15;
            } else  {
                if (speedFlag != 20) {
                    roadOverlay.setPoints(points);
                    map.getOverlays().add(roadOverlay);
                    roadOverlay = new Polyline();
                    points = new ArrayList<>();
                }
                roadOverlay.setColor(getResources().getColor(R.color.colorDanger));
                speedFlag = 20;
            }
            map.invalidate();
        }

    }
}

