package com.example.theatreticketsapp;

import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;

public class Venue implements Parcelable {

    private String name, postcode, strLocation, description;
    private Location location;

    public Venue(){
        location = null;
    }

    public Venue(String name, String postcode, String description){
        this.name = name;
        this.postcode = postcode;
        this.strLocation = name + ", " + postcode;
        this.description = description;
    }

    public String getStrLocation(){
        return this.strLocation;
    }

    public String getVenueName(){
        return this.name;
    }

    public String getDescription(){return this.description;}

    public void setLocation(Location location){
        this.location = location;
    }

    public Location getLocation(){
        return this.location;
    }


    protected Venue(Parcel in) {
        name = in.readString();
        postcode = in.readString();
        strLocation = in.readString();
        description = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
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
        dest.writeString(strLocation);
        dest.writeString(description);
        dest.writeParcelable(location, flags);
    }
}
