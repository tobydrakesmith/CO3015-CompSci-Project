package com.example.theatreticketsapp;

public class Review {

    private int rating;
    private String ratingTxt, date, showName;
    private String userName, userLastName;
    private int userid, bookingid, showinstanceid;

    public Review(int rating, String ratingText, String date, String showName, int userid, int bookingid, int showinstanceid){

        this.rating = rating;
        this.ratingTxt = ratingText;
        this.date = date;
        this.showName = showName;
        this.userid = userid;
        this.bookingid = bookingid;
        this.showinstanceid = showinstanceid;
    }

    public int getRating(){
        return this.rating;
    }

    public String getDate() {
        return date;
    }

    public String getShowName() {
        return showName;
    }

    public int getUserid() {
        return userid;
    }

    public int getBookingid() {
        return bookingid;
    }

    public int getShowinstanceid() {
        return showinstanceid;
    }

    public String getRatingTxt() {
        return ratingTxt;
    }
}
