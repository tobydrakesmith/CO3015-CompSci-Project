package com.example.theatreticketsapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BasketBooking implements Parcelable {

    private ArrayList<Ticket> tickets;
    private String date, startTime;
    private Show show;
    private int bookingID;
    private int tempID;

    public BasketBooking(String date, String startTime, Show show, int tempID){
        tickets = new ArrayList<>();
        this.date = date;
        this.startTime = startTime;
        this.show = show;
        this.tempID = tempID;
    }

    public BasketBooking() {
        tickets = new ArrayList<>();
    }

    public void addTicket(Ticket ticket){
        tickets.add(ticket);
    }

    public String getDate(){
        return this.date;
    }

    public String getStartTime(){
        return this.startTime;
    }

    public String getShowName(){
        return this.show.getShowName();
    }

    public int getTempID(){
        return this.tempID;
    }

    public int getNumberOfTickets(){
        return tickets.size();
    }

    public void setBookingID(int bookingID){
        this.bookingID = bookingID;
    }


    public int getBookingID(){
        return bookingID;
    }

    public Show getShow(){
        return this.show;
    }

    public int getCost(){
        int toRet = 0;
        for (Ticket ticket: tickets) {
            toRet+= ticket.getPrice();
        }
        return toRet;
    }


    public Ticket getTicket(int pos){
        return tickets.get(pos);
    }

    public ArrayList<Ticket> getTickets(){
        return tickets;
    }



    //parcelling

    /*private ArrayList<Ticket> tickets;
    private String date, startTime;
    private Show show;*/

    protected BasketBooking(Parcel in){
        tickets = in.readArrayList(Ticket.class.getClassLoader());
        date = in.readString();
        startTime = in.readString();
        show = in.readParcelable(Show.class.getClassLoader());
        tempID = in.readInt();


    }

    public static final Creator<BasketBooking> CREATOR = new Creator<BasketBooking>() {
        @Override
        public BasketBooking createFromParcel(Parcel in) {
            return new BasketBooking(in);
        }

        @Override
        public BasketBooking[] newArray(int size) {
            return new BasketBooking[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(tickets);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeParcelable(show, flags);
        dest.writeInt(tempID);

    }
}
