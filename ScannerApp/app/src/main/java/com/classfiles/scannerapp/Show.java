package com.classfiles.scannerapp;

public class Show {
    private String name, matStart, eveStart;
    private boolean monMat, monEve, tueMat, tueEve, wedMat, wedEve, thuMat, thuEve, friMat, friEve,
            satMat, satEve, sunMat, sunEve;


    public Show(String name, boolean monMat, boolean monEve, boolean tueMat, boolean tueEve,
                boolean wedMat, boolean wedEve, boolean thuMat, boolean thuEve, boolean friMat,
                boolean friEve, boolean satMat, boolean satEve, boolean sunMat, boolean sunEve,
                String matStart, String eveStart) {
        this.name = name;
        this.monMat = monMat;
        this.monEve = monEve;
        this.tueMat = tueMat;
        this.tueEve = tueEve;
        this.wedMat = wedMat;
        this.wedEve = wedEve;
        this.thuMat = thuMat;
        this.thuEve = thuEve;
        this.friMat = friMat;
        this.friEve = friEve;
        this.satMat = satMat;
        this.satEve = satEve;
        this.sunMat = sunMat;
        this.sunEve = sunEve;
        this.matStart = matStart;
        this.eveStart = eveStart;
    }

    public String getName() {
        return name;
    }

    public boolean isMonMat() {
        return monMat;
    }

    public boolean isMonEve() {
        return monEve;
    }

    public boolean isTueMat() {
        return tueMat;
    }

    public boolean isTueEve() {
        return tueEve;
    }

    public boolean isWedMat() {
        return wedMat;
    }

    public boolean isWedEve() {
        return wedEve;
    }

    public boolean isThuMat() {
        return thuMat;
    }

    public boolean isThuEve() {
        return thuEve;
    }

    public boolean isFriMat() {
        return friMat;
    }

    public boolean isFriEve() {
        return friEve;
    }

    public boolean isSatMat() {
        return satMat;
    }

    public boolean isSatEve() {
        return satEve;
    }

    public boolean isSunMat() {
        return sunMat;
    }

    public boolean isSunEve() {
        return sunEve;
    }

    public String getMatStart(){
        return matStart;
    }

    public String getEveStart(){
        return eveStart;
    }
}
