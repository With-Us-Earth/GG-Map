package com.example.ggmap;


public class SungBookCCTV {
    private String address;
    private double latitude;
    private double longitude;

    public SungBookCCTV() {

    }

    public SungBookCCTV(String address, double latitude, double longitude) {
        this.address = address;
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