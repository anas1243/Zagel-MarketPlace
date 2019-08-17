package com.example.zagelx.Models;

import java.io.Serializable;

public class LocationInfoForUsers implements Serializable {
    private String uLat = "";
    private String uLng = "";
    private String uAdminArea = "";      //el mo7afza
    private String uSubAdmin = "";       //el markaz
    private String uLocality = "";       //el 7ay

    public LocationInfoForUsers() {
    }

    public LocationInfoForUsers(String uLat, String uLng, String uAdminArea,
                                 String uSubAdmin, String uLocality) {
        this.uLat = uLat;
        this.uLng = uLng;
        this.uAdminArea = uAdminArea;
        this.uSubAdmin = uSubAdmin;
        this.uLocality = uLocality;
    }

    public String getuLat() {
        return uLat;
    }

    public String getuLng() {
        return uLng;
    }

    public String getuAdminArea() {
        return uAdminArea;
    }

    public String getuSubAdmin() {
        return uSubAdmin;
    }

    public String getuLocality() {
        return uLocality;
    }
}