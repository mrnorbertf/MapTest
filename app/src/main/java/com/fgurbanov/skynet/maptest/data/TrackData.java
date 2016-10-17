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
    // здесь можно задать граничные скорости
    // в коротрый произойдет переход в цветовой гамме
    public static double COLOR_Blue_to_Green =  5;
    public static double COLOR_Green_to_Yellow = 10;
    public static double COLOR_Yellow_to_Red = 12.5;
    public static double COLOR_Max = 20;
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
        if( i < trackPointses.size()) {
            // find AVG speed in the area
            double speed = trackPointses.get(i).getSpeed();


            float[] hsv = {0,1,1};
            //hsv[1] = (float) (180 / COLOR_STEP * speed);
            if (speed < COLOR_Blue_to_Green) {
                hsv[0] = (float) (240 - (60/COLOR_Blue_to_Green) * speed);
            }
            else if (speed < COLOR_Green_to_Yellow) {
                hsv[0] = (float) ( (180*COLOR_Green_to_Yellow-90*COLOR_Blue_to_Green - 90*speed)/(COLOR_Green_to_Yellow - COLOR_Blue_to_Green));
            } else if (speed < COLOR_Yellow_to_Red) {
                hsv[0] = (float)  ( (90*COLOR_Yellow_to_Red - 30*COLOR_Green_to_Yellow -60*speed) / (COLOR_Yellow_to_Red - COLOR_Green_to_Yellow)) ;
            }
            else if (speed < COLOR_Max){
                hsv[0] = (float) ( (30*COLOR_Max - 30*speed )/(COLOR_Max - COLOR_Yellow_to_Red) );
            }
            color = Color.HSVToColor(hsv);
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
