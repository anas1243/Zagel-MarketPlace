package com.example.zagelx.Models;

public class Trips {

    private int delegateImage = 0;//will be removed
    private String delegateID = "";

    private String routeDate = "";
    private String routePrice = "";

    private String source = "";
    private String destination = "";
    private int vehicleImage = 0;

    public Trips() {
    }

    public Trips(int delegateImage, String delegateID
            , String delvieryDate, String deliveryPrice,
                 String source, String destination, int vehicleImage) {
        this.delegateImage = delegateImage;
        this.delegateID = delegateID;
        this.routeDate = delvieryDate;
        this.routePrice = deliveryPrice;
        this.source = source;
        this.destination = destination;
        this.vehicleImage = vehicleImage;
    }

    public int getDelegateImage() {
        return delegateImage;
    }

    public String getDelegateID() {
        return delegateID;
    }

    public String getRouteDate() {
        return routeDate;
    }

    public String getRoutePrice() {
        return routePrice;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getVehicleImage() {
        return vehicleImage;
    }
}
