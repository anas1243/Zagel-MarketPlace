package com.example.zagelx.Models;

public class Orders {
    private String merchantId = "";

    private String packageName = "";
    private String packageImageURL = "";
    private String packageDescription = "";
    private String packagePrice = "";
    private boolean isPrePaid = false;

    private String acceptedDelegateID = "";
    private BirthDate deliveryDate = null;
    private String deliveryPrice = "";

    private String vehicle = "";
    private String source = "";
    private String destination = "";
    private String endConcumerMobile = "";

    private String packageState = "";


    public Orders() {
    }

    public Orders(String merchantId, String packageImageURL,
                  String packageName, BirthDate delvieryDate, String deliveryPrice,
                  String source, String destination) {
        this.merchantId = merchantId;
        this.packageImageURL = packageImageURL;
        this.packageName = packageName;
        this.deliveryDate = delvieryDate;
        this.deliveryPrice = deliveryPrice;
        this.source = source;
        this.destination = destination;
    }



    public String getMerchantId() {
        return merchantId;
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
    public String getEndConcumerMobile() {
        return endConcumerMobile;
    }

    public String getPackageState() {
        return packageState;
    }











}
