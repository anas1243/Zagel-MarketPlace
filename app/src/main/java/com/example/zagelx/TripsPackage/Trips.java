package com.example.zagelx.TripsPackage;

public class Trips {

    private int delegateImage;
    private String delegateName;

    private String routeDate;
    private String routePrice;

    private String source;
    private String destination;
    private int vehicleImage;

    public Trips() {
    }

    public Trips(int delegateImage, String delegateName
            , String delvieryDate, String deliveryPrice,
                 String source, String destination, int vehicleImage) {
        this.delegateImage = delegateImage;
        this.delegateName = delegateName;
        this.routeDate = delvieryDate;
        this.routePrice = deliveryPrice;
        this.source = source;
        this.destination = destination;
        this.vehicleImage = vehicleImage;
    }

    public int getDelegateImage() {
        return delegateImage;
    }

    public String getDelegateName() {
        return delegateName;
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
