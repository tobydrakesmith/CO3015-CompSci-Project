package com.example.theatreticketsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Ticket implements Parcelable {

    private int price;
    private Show show;
    private String priceBand;

    Ticket(int price, Show show, String priceBand){
        this.price = price;
        this.show = show;
        this.priceBand = priceBand;
    }

    Ticket(int price, String priceBand){
        this.price = price;
        this.priceBand = priceBand;
    }



    public int getPrice() {
        return price;
    }



    public Show getShow(){ return show; }



    public String getPriceBand(){
        return this.priceBand;
    }

    protected Ticket(Parcel in){

        price = in.readInt();
        show = in.readParcelable(Show.class.getClassLoader());
        priceBand = in.readString();

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
        dest.writeString(priceBand);
    }

}
