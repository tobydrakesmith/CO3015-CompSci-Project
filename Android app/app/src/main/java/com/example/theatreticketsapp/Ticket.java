package com.example.theatreticketsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Ticket implements Parcelable {

    private int price;
    private Show show;
    private int ticketid;

    Ticket(int price, Show show){
        this.price = price;
        this.show = show;
    }

    Ticket(int ticketid, int price){
        this.ticketid = ticketid;
        this.price = price;
    }


    public int getPrice() {
        return price;
    }



    public Show getShow(){ return show; }


    public int getTicketid(){
        return this.ticketid;
    }

    protected Ticket(Parcel in){

        price = in.readInt();
        show = in.readParcelable(Show.class.getClassLoader());

    }

    public static final Creator<Ticket> CREATOR = new Parcelable.Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeInt(price);
        dest.writeParcelable(show, flags);
    }

}
