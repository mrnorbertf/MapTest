package com.fgurbanov.skynet.maptest.data;

import com.google.android.gms.maps.model.LatLng;


import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by SkyNet on 10.10.2016.
 */

public class TrackPoints implements Serializable{

    //
    // private LatLng coordinates;
    private double latitude;
    private double longitude;
    private double altitude;
    private long timeStamp;
    private double heartRate;
    private double speed;
    private int direction;


    public TrackPoints( LatLng coordinates, double altitude, long timeStamp, double heartRate,
                        double speed, int direction) {
        latitude = coordinates.latitude;
        longitude = coordinates.longitude;
        this.altitude = altitude;
        this.timeStamp = timeStamp;
        this.heartRate = heartRate;
        this.speed = speed;
        this.direction = direction;
    }

    public GeoPoint getGeoPoint() {
        GeoPoint geoPoint = new GeoPoint(latitude, longitude);
        return  geoPoint;
    }

    public LatLng getCoordinates()   {
        LatLng coordinates = new LatLng(latitude, longitude);
        return coordinates;
    }

    public double getLongitude()  {
        return longitude;
    }

    public double getLatitude()  {
        return longitude;
    }
    public double getAltitude() {
        return altitude;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double getHeartRate() {
        return heartRate;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setCoordinates(LatLng coordinates) {
        latitude = coordinates.latitude;
        longitude = coordinates.longitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setHeartRate(double heartRate) {
        this.heartRate = heartRate;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

}
