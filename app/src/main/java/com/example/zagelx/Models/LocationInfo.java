package com.example.zagelx.Models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LocationInfo implements Serializable {
    String sLat = "";
    String sLng = "";
    String sAdminArea = "";      //el mo7afza
    String sSubAdmin = "";       //el markaz
    String sLocality = "";       //el 7ay

    String dLat = "";
    String dLng = "";
    String dAdminArea = "";      //el mo7afza
    String dSubAdmin = "";       //el markaz
    String dLocality = "";       //el 7ay


    public LocationInfo() {
    }

    public String getsLat() {
        return sLat;
    }

    public String getsLng() {
        return sLng;
    }


    public String getsAdminArea() {
        return sAdminArea;
    }

    public String getsSubAdmin() {
        return sSubAdmin;
    }

    public String getsLocality() {
        return sLocality;
    }

    public void setsLat(String sLat) {
        this.sLat = sLat;
    }

    public void setsLng(String sLng) {
        this.sLng = sLng;
    }

    public void setsAdminArea(String sAdminArea) {
        this.sAdminArea = sAdminArea;
    }

    public void setsSubAdmin(String sSubAdmin) {
        this.sSubAdmin = sSubAdmin;
    }

    public void setsLocality(String sLocality) {
        this.sLocality = sLocality;
    }

    public void setSLocationInfo(String sLat, String sLng, String sAdminArea,
                                 String sSubAdmin, String sLocality) {
        this.sLat = sLat;
        this.sLng = sLng;
        this.sAdminArea = sAdminArea;
        this.sSubAdmin = sSubAdmin;
        this.sLocality = sLocality;
    }

    public void setdLocationInfo( String dLat, String dLng, String dAdminArea,
                                  String dSubAdmin, String dLocality) {
        this.dLat = dLat;
        this.dLng = dLng;
        this.dAdminArea = dAdminArea;
        this.dSubAdmin = dSubAdmin;
        this.dLocality = dLocality;
    }

    public void setdLat(String dLat) {
        this.dLat = dLat;
    }

    public void setdLng(String dLng) {
        this.dLng = dLng;
    }

    public void setdAdminArea(String dAdminArea) {
        this.dAdminArea = dAdminArea;
    }

    public void setdSubAdmin(String dSubAdmin) {
        this.dSubAdmin = dSubAdmin;
    }

    public void setdLocality(String dLocality) {
        this.dLocality = dLocality;
    }

    public String getdLat() {
        return dLat;
    }

    public String getdLng() {
        return dLng;
    }

    public String getdAdminArea() {
        return dAdminArea;
    }

    public String getdSubAdmin() {
        return dSubAdmin;
    }

    public String getdLocality() {
        return dLocality;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "sLat='" + sLat + '\'' +
                ", sLng='" + sLng + '\'' +
                ", sAdminArea='" + sAdminArea + '\'' +
                ", sSubAdmin='" + sSubAdmin + '\'' +
                ", sLocality='" + sLocality + '\'' +
                ", dLat='" + dLat + '\'' +
                ", dLng='" + dLng + '\'' +
                ", dAdminArea='" + dAdminArea + '\'' +
                ", dSubAdmin='" + dSubAdmin + '\'' +
                ", dLocality='" + dLocality + '\'' +
                '}';
    }
    public String fullSourceLocation(){
        return sAdminArea+", "+sSubAdmin;
    }
    public String fullDestinationLocation(){
        return dAdminArea+", "+dSubAdmin;
    }

}
