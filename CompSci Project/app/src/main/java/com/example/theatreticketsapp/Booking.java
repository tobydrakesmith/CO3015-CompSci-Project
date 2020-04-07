package com.example.theatreticketsapp;


import android.os.Parcel;
import android.os.Parcelable;

public class Booking implements Parcelable {

    private int showInstanceID, numberOfTickets, userID, bookingID;
    private String date, showName, showTime;

    Booking(int bookingID, int showID, int numberOfTickets, int userID, String date, String showName, String showTime){
        this.bookingID = bookingID;
        this.showInstanceID = showID;
        this.numberOfTickets = numberOfTickets;
        this.userID = userID;
        this.date = date;
        this.showName = showName;
        this.showTime = showTime;
    }


    public String getShowName(){
        return this.showName;
    }

    public String getDate(){
        return this.date;
    }

    public String getShowTime(){ return this.showTime;}


    public int getNumberOfTickets(){
        return this.numberOfTickets;
    }

    public void setShowName(String showName){
        this.showName = showName;
    }

    public int getShowInstanceID(){
        return this.showInstanceID;
    }

    public int getBookingID(){
        return this.bookingID;
    }





    protected Booking(Parcel in){
        showInstanceID = in.readInt();
        bookingID = in.readInt();
        showName = in.readString();
        numberOfTickets = in.readInt();
        showTime = in.readString();
        date = in.readString();
    }

    public static final Creator<Booking> CREATOR = new Parcelable.Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeInt(showInstanceID);
        dest.writeInt(bookingID);
        dest.writeString(showName);
        dest.writeInt(numberOfTickets);
        dest.writeString(showTime);
        dest.writeString(date);
    }



}
