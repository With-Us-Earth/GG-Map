package com.example.ggmap;

public class PersonCount {
    double startLatitude;
    double startLongitude;
    double endLatitude;
    double endLongitude;
    int count;

    public double getStartLatitude() {
        return startLatitude;
    }
    public double getEndLatitude() {
        return endLatitude;
    }
    public double getStartLongitude() {
        return startLongitude;
    }
    public double getEndLongitude() {
        return endLongitude;
    }
    public int getCount() { return count;}

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }
    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }
    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }
    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public PersonCount() {}

    public PersonCount(double startLatitude, double startLongitude, double endLatitude, double endLongitude, int count) {
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude= endLongitude;
        this.count = count;
    }


}
