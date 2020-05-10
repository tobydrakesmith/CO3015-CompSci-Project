package com.classfiles.scannerapp;

public class Venue {

    private int id;
    private String name, postcode, city;

    public Venue(int id, String name, String postcode, String city){
        this.name = name;
        this.postcode = postcode;
        this.city = city;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }
}
