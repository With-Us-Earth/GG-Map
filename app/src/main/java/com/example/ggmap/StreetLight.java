package com.example.ggmap;

public class StreetLight {
    private int Column1;
    private String name;
    private double latitude;
    private double longitude;

    public StreetLight(){

    }

    public StreetLight(double latitude, String name, int Column1, double longitude){
        this.Column1 = Column1;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // set function
    public void setLatitude(double latitude){ this.latitude = latitude; }
    public void setLongitude(double longitude){ this.longitude = longitude; }

    // get function
    public double getLatitude(){ return this.latitude; }
    public double getLongitude(){ return this.longitude; }
}
