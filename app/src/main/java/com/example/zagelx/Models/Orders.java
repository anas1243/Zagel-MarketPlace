package com.example.zagelx.Models;

import java.io.Serializable;

public class Orders implements Serializable {
    private String merchantId = "";
    private String merchantImageURL = "";
    private String merchantName = "";

    private String packageName = "";
    private String packageImageURL = "";
    private String packageDescription = "";
    private String packagePrice = "";
    private boolean isPrePaid = false;
    private boolean isBreakable = false;

    private String acceptedDelegateID = "";
    private BirthDate deliveryDate = null;
    private String deliveryPrice = "";

    private String vehicle = "";
    private String source = "";
    private String destination = "";
    private String endConsumerMobile = "";

    private String packageState = "";

    private LocationPair currentOrderSourceLocation;
    private LocationPair currentOrderDestinationLocation;


    public Orders() {
    }


    public Orders(String merchantId, String merchantImageURL, String merchantName, String packageName, String packageImageURL,
                  String packageDescription, String packagePrice, boolean isPrePaid, boolean isBreakable,
                  BirthDate deliveryDate, String deliveryPrice, String vehicle,
                  String source, String destination, String endConsumerMobile, LocationPair currentOrderSourceLocation, LocationPair currentOrderDestinationLocation) {
        this.merchantId = merchantId;
        this.merchantImageURL = merchantImageURL;
        this.merchantName = merchantName;

        this.packageName = packageName;
        this.packageImageURL = packageImageURL;
        this.packageDescription = packageDescription;
        this.packagePrice = packagePrice;
        this.isPrePaid = isPrePaid;
        this.isBreakable = isBreakable;
        this.deliveryDate = deliveryDate;
        this.deliveryPrice = deliveryPrice;
        this.vehicle = vehicle;
        this.source = source;
        this.destination = destination;
        this.endConsumerMobile = endConsumerMobile;
        this.currentOrderSourceLocation = currentOrderSourceLocation;
        this.currentOrderDestinationLocation = currentOrderDestinationLocation;
    }

    public LocationPair getCurrentOrderDestinationLocation() {
        return currentOrderDestinationLocation;
    }

    public LocationPair getCurrentOrderSourceLocation() {
        return currentOrderSourceLocation;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getMerchantImageURL() {
        return merchantImageURL;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackageImageURL() {
        return packageImageURL;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public boolean isPrePaid() {
        return isPrePaid;
    }

    public String getAcceptedDelegateID() {
        return acceptedDelegateID;
    }

    public BirthDate getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getEndConsumerMobile() {
        return endConsumerMobile;
    }

    public String getPackageState() {
        return packageState;
    }

    public boolean isBreakable() {
        return isBreakable;
    }


}
