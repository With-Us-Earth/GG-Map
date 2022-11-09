package com.example.ggmap;

public class Camera {
    double latitude;
    double longitude;
    String key;

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public Camera(){}


   public Camera(String key, Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
   }
}




