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
    private String endConsumerMobile = "";

    private String packageState = "";

    private LocationInfo currentOrderLocationInfo;


    public Orders() {
    }


    public Orders(String merchantId, String merchantImageURL, String merchantName, String packageName, String packageImageURL,
                  String packageDescription, String packagePrice, boolean isPrePaid, boolean isBreakable,
                  BirthDate deliveryDate, String deliveryPrice, String vehicle,
                  String endConsumerMobile, LocationInfo currentOrderLocationInfo) {
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
        this.endConsumerMobile = endConsumerMobile;
        this.currentOrderLocationInfo = currentOrderLocationInfo;
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

    public String getEndConsumerMobile() {
        return endConsumerMobile;
    }

    public String getPackageState() {
        return packageState;
    }

    public boolean isBreakable() {
        return isBreakable;
    }

    public LocationInfo getCurrentOrderLocationInfo() {
        return currentOrderLocationInfo;
    }

    public void setMerchantImageURL(String merchantImageURL) {
        this.merchantImageURL = merchantImageURL;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
