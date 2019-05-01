package com.example.zagelx.Models;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LocationInfo implements Serializable {

    private  LatLng orderSourceLatLng;
    private  LatLng orderDestinationLatLng;
    private  Address sourceAddress;
    private  Address destinationAddress;

    public LocationInfo() {
    }

    public LatLng getOrderSourceLatLng() {
        return orderSourceLatLng;
    }

    public LatLng getOrderDestinationLatLng() {
        return orderDestinationLatLng;
    }

    public Address getSourceAddress() {
        return sourceAddress;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public LocationInfo(LatLng orderSourceLatLng, LatLng orderDestinationLatLng, Address sourceAddress, Address destinationAddress) {
        this.orderSourceLatLng = orderSourceLatLng;
        this.orderDestinationLatLng = orderDestinationLatLng;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;


    }

    public void setOrderSourceLatLng(LatLng orderSourceLatLng) {
        this.orderSourceLatLng = orderSourceLatLng;
    }

    public void setOrderDestinationLatLng(LatLng orderDestinationLatLng) {
        this.orderDestinationLatLng = orderDestinationLatLng;
    }

    public void setSourceAddress(Address sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public void setDestinationAddress(Address destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "orderSourceLatLng=" + orderSourceLatLng +
                ", orderDestinationLatLng=" + orderDestinationLatLng +
                ", sourceAddress=" + sourceAddress +
                ", destinationAddress=" + destinationAddress +
                '}';
    }
}
