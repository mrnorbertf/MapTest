package com.fgurbanov.skynet.maptest.data;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SkyNet on 10.10.2016.
 * this class illustrate information about track
 */

public class TrackData implements Serializable {
    private String type;
    private String dt_start;
    private String dt_end;
/*
    private int time;
    private double distance;
    private int idTrack;
    private double spAvg;
    private double spMax;
    private double calories;
    /// ??????
    private int access;
    private double weight;
    private double cardio;
    private double hrMax;
    private double hrAvg;
    private double varMax;
    private double varMin;
    private boolean status;
*/
    public ArrayList<TrackPoints> getTrackPointses() {
        return trackPointses;
    }

    public void setTrackPointses(ArrayList<TrackPoints> trackPointses) {
        this.trackPointses = trackPointses;
    }

    private ArrayList<TrackPoints> trackPointses;

    public TrackData() {
    }
    /*
    public TrackData(String type, Date dt_start, Date dt_end, int time, double distance,
                     int idTrack, double spAvg, double spMax, double calories, int access,
                     double weight, double cardio, double hrMax, double hrAvg, double varMax,
                     double varMin, boolean status) {

        this.type = type;
        this.dt_start = dt_start;
        this.dt_end = dt_end;

        this.time = time;
        this.distance = distance;
        this.idTrack = idTrack;
        this.spAvg = spAvg;
        this.spMax = spMax;
        this.calories = calories;
        this.access = access;
        this.weight = weight;
        this.cardio = cardio;
        this.hrMax = hrMax;
        this.hrAvg = hrAvg;
        this.varMax = varMax;
        this.varMin = varMin;
        this.status = status;

    }
    */
    public TrackData(String type, String dt_start, String dt_end) {
        this.type = type;
        this.dt_start = dt_start;
        this.dt_end = dt_end;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDt_start() {
        return dt_start;
    }

    public void setDt_start(String dt_start) {
        this.dt_start = dt_start;
    }

    public String getDt_end() {
        return dt_end;
    }

    public void setDt_end(String dt_end) {
        this.dt_end = dt_end;
    }

    public int getColor(int i){
        int color = 255;
        if( i < (trackPointses.size()-1)) {
            // find AVG speed in the area
            double speed = (trackPointses.get(i).getSpeed() + trackPointses.get(i+1).getSpeed()) / 2;
            // red
            int red;

            if (speed > 7.5){
                red = (int) (-12.542*speed*speed + 361.03*speed - 2345.9 );
            } else if (speed >= 12.5){red = 255;}
            else {red = 0;}
/*
            if(speed >10){
                red = (int )(22.8*Math.sqrt(speed*speed -100));
            } else if (speed >= 15) {red = 255;}
*/
            // green
            int green;
            if (speed > 7.5) {
                green = (int) (-12.854*speed * speed + 339.94*speed - 1994.7);
            } else if (speed >= 17.5) {green = 0;}
            else {green = 255;}

            // blue
            int blue;
            if (speed > 7.5 ) {blue = 0;}
            else if (speed <2.5) {blue = 255;}
            else {blue = (int) (-13* speed* speed+ 82.863*speed + 119.38);}
            color = Color.rgb(red, green, blue);
        }
        return color;
    }

   /*
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getIdTrack() {
        return idTrack;
    }

    public void setIdTrack(int idTrack) {
        this.idTrack = idTrack;
    }

    public double getSpAvg() {
        return spAvg;
    }

    public void setSpAvg(double spAvg) {
        this.spAvg = spAvg;
    }

    public double getSpMax() {
        return spMax;
    }

    public void setSpMax(double spMax) {
        this.spMax = spMax;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCardio() {
        return cardio;
    }

    public void setCardio(double cardio) {
        this.cardio = cardio;
    }

    public double getHrMax() {
        return hrMax;
    }

    public void setHrMax(double hrMax) {
        this.hrMax = hrMax;
    }

    public double getHrAvg() {
        return hrAvg;
    }

    public void setHrAvg(double hrAvg) {
        this.hrAvg = hrAvg;
    }

    public double getVarMax() {
        return varMax;
    }

    public void setVarMax(double varMax) {
        this.varMax = varMax;
    }

    public double getVarMin() {
        return varMin;
    }

    public void setVarMin(double varMin) {
        this.varMin = varMin;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    */
}
