package com.example.theatreticketsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Ticket implements Parcelable {
    private String date;
    private int price;
    private Show show;

    Ticket(int price, String date, Show show){
        this.date = date;
        this.price = price;
        this.show = show;
    }


    public int getPrice() {
        return price;
    }

    public String getDate(){return date;}

    public Show getShow(){ return show; }



    protected Ticket(Parcel in){

        date = in.readString();
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

        dest.writeString(date);
        dest.writeInt(price);
        dest.writeParcelable(show, flags);
    }

}
