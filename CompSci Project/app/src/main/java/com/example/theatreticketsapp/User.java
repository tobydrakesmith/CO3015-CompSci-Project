package com.example.theatreticketsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String email, firstName, lastName;
    private int id;

    public User(int id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(){
        this.id = -1;
    }

    public int getId() {
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    protected User(Parcel in){

        id = in.readInt();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();

    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);

    }
}
