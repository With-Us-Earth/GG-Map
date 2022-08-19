package com.example.ggmap;

public class GwangjinStreetLight {
    public String name;
    public double latitude;
    public double longitude;

    // set function
    public void setLatitude(double latitude){ this.latitude = latitude; }
    public void setLongitude(double longitude){ this.longitude = longitude; }

    // get function
    public double getLatitude(){ return this.latitude; }
    public double getLongitude(){ return this.longitude; }
}
