package com.example.zagelx.Models;

public class LocationPair {
    String lat = "";
    String lng = "";

    public LocationPair(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LocationPair() {
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
