package com.example.theatreticketsapp;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.Comparator;

public class Show implements Parcelable {

    private String showName, venueName, showDescription, startDate, endDate, matineeStart, eveningStart;
    private int monMat, monEve, tueMat, tueEve, wedMat, wedEve, thuMat, thuEve, friMat, friEve, satMat, satEve, sunMat, sunEve;
    private int pricePbA, pricePbB, pricePbC, pricePbD, numTicketsPbA, numTicketsPbB, numTicketsPbC, numTicketsPbD;
    private int id;
    private double rating;
    private int runningTime;
    private Venue venue;
    private float userDistanceFromVenue;
    private static DecimalFormat df = new DecimalFormat("0.00");



    public Show(int id, String showName, String venueName, String startDate, String endDate, String matineeStart, String eveningStart,
                int monMat, int monEve, int tueMat, int tueEve, int wedMat, int wedEve, int thuMat,
                int thuEve, int friMat, int friEve, int satMat, int satEve, int sunMat, int sunEve,
                int pricePbA, int pricePbB, int pricePbC, int pricePbD, int numTicketsPbA, int numTicketsPbB, int numTicketsPbC, int numTicketsPbD, Venue venue) {
        this.id = id;
        this.showName = showName;
        this.venueName = venueName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.matineeStart = matineeStart;
        this.eveningStart = eveningStart;
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
        this.pricePbA = pricePbA;
        this.pricePbB = pricePbB;
        this.pricePbC = pricePbC;
        this.pricePbD = pricePbD;
        this.numTicketsPbA = numTicketsPbA;
        this.numTicketsPbB = numTicketsPbB;
        this.numTicketsPbC = numTicketsPbC;
        this.numTicketsPbD = numTicketsPbD;

        rating=0;
        userDistanceFromVenue=0;
        this.venue = venue;
        runningTime = 0;
    }


    public static Comparator<Show> compareNames = new Comparator<Show>() {
        @Override
        public int compare(Show s1, Show s2) {
            return s1.getShowName().compareToIgnoreCase(s2.getShowName());
        }
    };

    public static Comparator<Show> compareRating = new Comparator<Show>() {
        @Override
        public int compare(Show s1, Show s2) {
            return (Double.compare(s2.getRating(), s1.getRating()));
        }
    };





    //GETTERS AND SETTERS:


    public void setShowName(String s){
        this.venueName = s;
    }

    public void setVenue(Venue venue){
        this.venue = venue;
    }

    public int getId(){
        return this.id;
    }


    public String getShowName() {
        return showName;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getShowDescription(){
        return showDescription;
    }

    public void setShowDescription(String showDescription) {
        this.showDescription = showDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getRunningTime(){
        return this.runningTime;
    }
    public void setRunningTime(int runningTime){
        this.runningTime = runningTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isMonMat() {
        return monMat ==1;
    }

    public boolean isMonEve() {
        return monEve ==1;
    }

    public boolean isTueMat() {
        return tueMat==1;
    }

    public boolean isTueEve() {
        return tueEve==1;
    }

    public boolean isWedMat() {
        return wedMat==1;
    }

    public boolean isWedEve() {
        return wedEve==1;
    }


    public boolean isThuMat() {
        return thuMat==1;
    }


    public boolean isThuEve() {
        return thuEve==1;
    }


    public boolean isFriMat() {
        return friMat==1;
    }


    public boolean isFriEve() { return friEve==1; }

    public boolean isSatMat() {
        return satMat==1;
    }


    public boolean isSatEve() {
        return satEve==1;
    }


    public boolean isSunMat() {
        return sunMat==1;
    }


    public boolean isSunEve() {
        return sunEve==1;
    }

    public String getMatineeStart(){ return matineeStart; }

    public String getEveningStart(){ return eveningStart; }


    public int getPricePbA() {
        return pricePbA;
    }

    public int getPricePbB() {
        return pricePbB;
    }

    public int getPricePbC() {
        return pricePbC;
    }


    public int getPricePbD() {
        return pricePbD;
    }


    public int getNumTicketsPbA() {
        return numTicketsPbA;
    }


    public int getNumTicketsPbB() {
        return numTicketsPbB;
    }


    public int getNumTicketsPbC() {
        return numTicketsPbC;
    }

    public int getNumTicketsPbD() {
        return numTicketsPbD;
    }

    public void setRating(double avgRating){
        this.rating = avgRating;
    }

    public double getRating(){
        return this.rating;
    }

    public String getUserDistanceFromVenue(Location userLocation){
        if (userDistanceFromVenue == 0){
            userDistanceFromVenue = (venue.getLocation().distanceTo(userLocation)) / 1000;
        }

        return df.format(userDistanceFromVenue);
    }




    //RELATED TO PARCELLING

    protected Show(Parcel in) {

        id = in.readInt();

        showName = in.readString();
        venueName = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        matineeStart = in.readString();
        eveningStart = in.readString();

        monMat = in.readInt();
        monEve = in.readInt();
        tueMat = in.readInt();
        tueEve = in.readInt();
        wedMat = in.readInt();
        wedEve = in.readInt();
        thuMat = in.readInt();
        thuEve = in.readInt();
        friMat = in.readInt();
        friEve = in.readInt();
        satMat = in.readInt();
        satEve = in.readInt();
        sunMat = in.readInt();
        sunEve = in.readInt();

        pricePbA = in.readInt();
        pricePbB = in.readInt();
        pricePbC = in.readInt();
        pricePbD = in.readInt();

        numTicketsPbA = in.readInt();
        numTicketsPbB = in.readInt();
        numTicketsPbC = in.readInt();
        numTicketsPbD = in.readInt();

        rating = in.readDouble();
        runningTime = in.readInt();

        venue = in.readParcelable(Venue.class.getClassLoader());
    }

    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);

        dest.writeString(showName);
        dest.writeString(venueName);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(matineeStart);
        dest.writeString(eveningStart);

        dest.writeInt(monMat);
        dest.writeInt(monEve);
        dest.writeInt(tueMat);
        dest.writeInt(tueEve);
        dest.writeInt(wedMat);
        dest.writeInt(wedEve);
        dest.writeInt(thuMat);
        dest.writeInt(thuEve);
        dest.writeInt(friMat);
        dest.writeInt(friEve);
        dest.writeInt(satMat);
        dest.writeInt(satEve);
        dest.writeInt(sunMat);
        dest.writeInt(sunEve);

        dest.writeInt(pricePbA);
        dest.writeInt(pricePbB);
        dest.writeInt(pricePbC);
        dest.writeInt(pricePbD);

        dest.writeInt(numTicketsPbA);
        dest.writeInt(numTicketsPbB);
        dest.writeInt(numTicketsPbC);
        dest.writeInt(numTicketsPbD);

        dest.writeDouble(rating);
        dest.writeInt(runningTime);

        dest.writeParcelable(venue, flags);


    }
}
