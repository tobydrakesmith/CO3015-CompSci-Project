package com.classfiles.theatreticketsapp;

public class Review {

    private String reviewTxt, date, showName;
    private int userid, bookingid, showinstanceid, rating, reviewid;

    public Review(int rating, String ratingText, String date, String showName, int userid, int bookingid, int showinstanceid) {

        this.rating = rating;
        this.reviewTxt = ratingText;
        this.date = date;
        this.showName = showName;
        this.userid = userid;
        this.bookingid = bookingid;
        this.showinstanceid = showinstanceid;
    }

    public Review(String ratingText, int rating, String date, String showName, int userid, int bookingid, int reviewid) {

        this.rating = rating;
        this.reviewTxt = ratingText;
        this.date = date;
        this.showName = showName;
        this.userid = userid;
        this.bookingid = bookingid;
        this.reviewid = reviewid;
    }

    public int getRating() {
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

    public String getReviewTxt() {
        return reviewTxt;
    }

    public int getReviewid() {
        return this.reviewid;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReviewTxt(String review) {
        this.reviewTxt = review;
    }
}
