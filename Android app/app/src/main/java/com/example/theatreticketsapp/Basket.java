package com.example.theatreticketsapp;

import android.os.CountDownTimer;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class Basket implements Parcelable{

    private final long COUNTDOWN_START = 60000;
    private final long COUNTDOWN_INTERVAL = 1000;

    private long timeOut;


    private ArrayList<BasketBooking> bookings;

    Basket(){
        bookings = new ArrayList<>();
        timeOut = System.currentTimeMillis() + COUNTDOWN_START;
    }

    private CountDownTimer countDownTimer = new CountDownTimer(System.currentTimeMillis() + COUNTDOWN_START, COUNTDOWN_INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {

        }
        @Override
        public void onFinish() {
            releaseTickets();
        }
    };

    public int size(){

        int toRet = 0;

        for (BasketBooking basketBooking : bookings) {
            toRet += basketBooking.getNumberOfTickets();
        }

        return toRet;
    }

    public boolean isEmpty(){
        return bookings.isEmpty();
    }

    public int getTotalCost(){
        int totalCost=0;

        for (BasketBooking basketBooking : bookings){
            totalCost += basketBooking.getCost();
        }


        return totalCost;
    }

    public ArrayList<Ticket> getTickets(int bookingID){

        for (BasketBooking basketBooking: bookings) {
            if (basketBooking.getBookingID() == bookingID) return basketBooking.getTickets();
        }

        return null;
    }


    public long getTimeOut(){
        return timeOut;
    }


    public void releaseTickets(BasketBooking booking){

        bookings.remove(booking);

        if (this.isEmpty()) {
            countDownTimer.cancel();
            timeOut = System.currentTimeMillis();
        }
    }

    public void releaseTickets(){
        bookings.clear();
    }


    public BasketBooking sameShow(BasketBooking booking){
        for (BasketBooking basketBooking : bookings) {

            if (basketBooking.getShowName().equals(booking.getShowName()) &&
                    (basketBooking.getDate()+basketBooking.getStartTime()).equals((booking.getDate()+booking.getStartTime()))){
                return basketBooking;
            }

        }
        return new BasketBooking();
    }


    public void addBooking(BasketBooking booking){

        if(this.isEmpty()) {
            countDownTimer.start();
            timeOut = System.currentTimeMillis() + COUNTDOWN_START;
        }

        BasketBooking check = sameShow(booking);
        if (check.getNumberOfTickets() == 0 ){
            bookings.add(booking);
        }
        else{
            for(int i=0; i<booking.getNumberOfTickets(); i++){
                check.addTicket(booking.getTicket(i));
            }
        }




    }


    public ArrayList<BasketBooking> getBookings(){
        return this.bookings;
    }

     // related to parcelling

    protected Basket(Parcel in){
        //TODO: Fix warning
        bookings = in.readArrayList(Ticket.class.getClassLoader());
        timeOut = in.readLong();
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

        dest.writeList(bookings);
        dest.writeLong(timeOut);
    }
}
