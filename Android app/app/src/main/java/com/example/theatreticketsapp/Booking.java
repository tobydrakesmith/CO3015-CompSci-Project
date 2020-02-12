package com.example.theatreticketsapp;


import java.util.ArrayList;

public class Booking {

    private int showInstanceID, numberOfTickets, userID, bookingID;
    private String date, showName;

    Booking(int showID, int numberOfTickets, int userID, String date, String showName){
        this.showInstanceID = showID;
        this.numberOfTickets = numberOfTickets;
        this.userID = userID;
        this.date = date;
        this.showName = showName;
    }


    public String getShowName(){
        return this.showName;
    }

    public String getDate(){
        return this.date;
    }

    public int getNumberOfTickets(){
        return this.numberOfTickets;
    }

    public void setShowName(String showName){
        this.showName = showName;
    }

    public int getShowInstanceID(){
        return this.showInstanceID;
    }



}
