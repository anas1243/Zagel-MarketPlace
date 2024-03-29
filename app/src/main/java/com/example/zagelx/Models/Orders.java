package com.example.zagelx.Models;

import java.io.Serializable;

public class Orders implements Serializable {
    private String orderId = "";
    private String merchantId = "";
    private String merchantImageURL = "";
    private String merchantName = "";
    private String merchantMobile = "";

    private LocationInfoForPackage locationInfoForPackage;
    private String packageName = "";
    private String packageImageURL = "";
    private String packageDescription = "";
    private String packagePrice = "";
    private String packageWeight = "";
    private BirthDate deliveryDate = null;
    private String deliveryPrice = "";
    private String acceptedDeliveryPrice = "";
    private boolean prePaid = false;
    private boolean breakable = false;
    private boolean verifiedUser = false;

    //triggers a cloud function that says to M that the D delegatePicked the order
    private boolean merchantDelivered = false;
    private boolean delegateDelivered = false;

    //triggers a cloud function that says to D merchantDelivered the order successfully
    private boolean delegatePicked = false;
    private boolean merchantPicked = false;

    private String vehicle = "";
    private String packageState = "";


    private RequestInfo currentRequestInfo;
    private int numberOfRequests = 0;

    private String acceptedDelegateID = "";
    private String acceptedDelegateName = "";
    private String acceptedDelegateMobile = "";
    private String endConsumerMobile = "";
    private String endConsumerName = "";

    public Orders() {
    }


    public Orders(String orderId, String merchantId, String merchantMobile
            , String merchantImageURL, String merchantName, String packageName
            , String packageWeight, String packageImageURL, String packageDescription
            , String packagePrice, boolean isPrePaid, boolean isBreakable, boolean verifiedUser
            , BirthDate deliveryDate, String deliveryPrice, String vehicle
            , String endConsumerMobile, String endConsumerName
            , LocationInfoForPackage locationInfoForPackage, String packageState) {
        this.orderId = orderId;
        this.merchantId = merchantId;
        this.merchantImageURL = merchantImageURL;
        this.merchantName = merchantName;

        this.packageName = packageName;
        this.packageImageURL = packageImageURL;
        this.packageDescription = packageDescription;
        this.packagePrice = packagePrice;
        this.packageWeight = packageWeight;
        this.prePaid = isPrePaid;
        this.breakable = isBreakable;
        this.deliveryDate = deliveryDate;
        this.deliveryPrice = deliveryPrice;
        this.vehicle = vehicle;
        this.endConsumerMobile = endConsumerMobile;
        this.locationInfoForPackage = locationInfoForPackage;
        this.packageState = packageState;
        this.verifiedUser = verifiedUser;
        this.endConsumerName = endConsumerName;
        this.merchantMobile = merchantMobile;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDelegatePicked(boolean delegatePicked) {
        this.delegatePicked = delegatePicked;
    }

    public void setMerchantPicked(boolean merchantPicked) {
        this.merchantPicked = merchantPicked;
    }

    public void setMerchantDelivered(boolean merchantDelivered) {
        this.merchantDelivered = merchantDelivered;
    }

    public void setDelegateDelivered(boolean delegateDelivered) {
        this.delegateDelivered = delegateDelivered;
    }

    public boolean isDelegateDelivered() {
        return delegateDelivered;
    }

    public boolean isMerchantPicked() {
        return merchantPicked;
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
        return prePaid;
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
        return breakable;
    }

    public boolean isVerifiedUser() {
        return verifiedUser;
    }

    public LocationInfoForPackage getLocationInfoForPackage() {
        return locationInfoForPackage;
    }

    public void setMerchantImageURL(String merchantImageURL) {
        this.merchantImageURL = merchantImageURL;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }


    public RequestInfo getCurrentRequestInfo() {
        return currentRequestInfo;
    }

    public void setCurrentRequestInfo(RequestInfo currentRequestInfo) {
        this.currentRequestInfo = currentRequestInfo;
    }

    public String getAcceptedDeliveryPrice() {
        return acceptedDeliveryPrice;
    }


    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId='" + orderId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", merchantImageURL='" + merchantImageURL + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", merchantMobile='" + merchantMobile + '\'' +
                ", LocationInfoForPackage=" + locationInfoForPackage +
                ", packageName='" + packageName + '\'' +
                ", packageImageURL='" + packageImageURL + '\'' +
                ", packageDescription='" + packageDescription + '\'' +
                ", packagePrice='" + packagePrice + '\'' +
                ", packageWeight='" + packageWeight + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", deliveryPrice='" + deliveryPrice + '\'' +
                ", prePaid=" + prePaid +
                ", breakable=" + breakable +
                ", verifiedUser=" + verifiedUser +
                ", merchantDelivered=" + merchantDelivered +
                ", delegatePicked=" + delegatePicked +
                ", vehicle='" + vehicle + '\'' +
                ", packageState='" + packageState + '\'' +
                ", currentRequestInfo=" + currentRequestInfo +
                ", numberOfRequests=" + numberOfRequests +
                ", acceptedDelegateID='" + acceptedDelegateID + '\'' +
                ", acceptedDelegateName='" + acceptedDelegateName + '\'' +
                ", acceptedDelegateMobile='" + acceptedDelegateMobile + '\'' +
                ", endConsumerMobile='" + endConsumerMobile + '\'' +
                ", endConsumerName='" + endConsumerName + '\'' +
                '}';
    }

    public String getEndConsumerName() {
        return endConsumerName;
    }

    public void setAcceptedDelegateID(String acceptedDelegateID) {
        this.acceptedDelegateID = acceptedDelegateID;
    }

    public String getAcceptedDelegateName() {
        return acceptedDelegateName;
    }

    public String getAcceptedDelegateMobile() {
        return acceptedDelegateMobile;
    }

    public String getMerchantMobile() {
        return merchantMobile;
    }

    public boolean isMerchantDelivered() {
        return merchantDelivered;
    }

    public boolean isDelegatePicked() {
        return delegatePicked;
    }

    public String getPackageWeight() {
        return packageWeight;
    }
}
