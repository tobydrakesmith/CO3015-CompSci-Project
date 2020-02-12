package com.example.theatreticketsapp;

import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Basket implements Parcelable{

    private final long COUNTDOWN_START = 60000;
    private final long COUNTDOWN_INTERVAL = 1000;

    private ArrayList<Ticket> basket;
    private long milliLeft;



    Basket(){
        basket = new ArrayList<>();
        milliLeft = COUNTDOWN_START;
    }

    private CountDownTimer countDownTimer = new CountDownTimer(COUNTDOWN_START, COUNTDOWN_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            milliLeft = millisUntilFinished;
        }
        @Override
        public void onFinish() {
            releaseTickets();
        }
    };

    public int size(){
        return basket.size();
    }

    public boolean isEmpty(){
        return basket.size() == 0;
    }

    public int getTotalCost(){
        int totalCost=0;

        for(int i=0; i<basket.size();i++)
            totalCost += basket.get(i).getPrice();


        return totalCost;
    }

    public long getMilliLeft(){return milliLeft;}

    public void setMilliLeft(long milliLeft){
        this.milliLeft = milliLeft;
    }

    public void addTicket(Ticket t){

        if(basket.isEmpty())
            countDownTimer.start();

        basket.add(t);
    }

    public void releaseTickets(String showName){

        for (int i=0; i<basket.size(); i++){
            if (basket.get(i).getShow().getShowName().equals(showName)){
                basket.remove(i);
                i--;
            }
        }

        if (this.isEmpty()) {
            countDownTimer.cancel();
            milliLeft = 0;
        }
    }

    public void releaseTickets(){
        basket.clear();
    }

    public Ticket getTicket(int i){
        return basket.get(i);
    }






     // related to parcelling

    protected Basket(Parcel in){
        //TODO: Fix warning
        basket = in.readArrayList(Ticket.class.getClassLoader());
        milliLeft = in.readLong();

    }

    public static final Creator<Basket> CREATOR = new Creator<Basket>() {
        @Override
        public Basket createFromParcel(Parcel in) {
            return new Basket(in);
        }

        @Override
        public Basket[] newArray(int size) {
            return new Basket[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeList(basket);
        dest.writeLong(milliLeft);

    }
}
