package com.example.theatreticketsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Venue implements Parcelable {

    private String name, postcode, location, description;

    public Venue(String name, String postcode, String description){
        this.name = name;
        this.postcode = postcode;
        this.location = name + ", " + postcode;
        this.description = description;
    }

    public String getLocation(){
        return this.location;
    }

    public String getVenueName(){
        return this.name;
    }

    public String getDescription(){return this.description;}



    protected Venue(Parcel in) {
        name = in.readString();
        postcode = in.readString();
        location = in.readString();
        description = in.readString();
    }

    public static final Creator<Venue> CREATOR = new Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(postcode);
        dest.writeString(location);
        dest.writeString(description);
    }
}
