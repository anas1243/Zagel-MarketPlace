package com.example.zagelx.Models;

import java.io.Serializable;

public class Orders implements Serializable {
    private String orderId = "";
    private String merchantId = "";
    private String merchantImageURL = "";
    private String merchantName = "";
    private String merchantMobile = "";

    private LocationInfo currentOrderLocationInfo;
    private String packageName = "";
    private String packageImageURL = "";
    private String packageDescription = "";
    private String packagePrice = "";
    private BirthDate deliveryDate = null;
    private String deliveryPrice = "";
    private boolean prePaid = false;
    private boolean breakable = false;
    private boolean verifiedUser = false;

    //triggers a cloud function that says to M that the D picked the order
    private boolean delivered = false;

    //triggers a cloud function that says to D delivered the order successfully
    private boolean picked = false;

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


    public Orders(String orderId, String merchantId, String merchantMobile, String merchantImageURL, String merchantName, String packageName, String packageImageURL,
                  String packageDescription, String packagePrice, boolean isPrePaid, boolean isBreakable, boolean verifiedUser,
                  BirthDate deliveryDate, String deliveryPrice, String vehicle,
                  String endConsumerMobile, String endConsumerName, LocationInfo currentOrderLocationInfo, String packageState) {
        this.orderId = orderId;
        this.merchantId = merchantId;
        this.merchantImageURL = merchantImageURL;
        this.merchantName = merchantName;

        this.packageName = packageName;
        this.packageImageURL = packageImageURL;
        this.packageDescription = packageDescription;
        this.packagePrice = packagePrice;
        this.prePaid = isPrePaid;
        this.breakable = isBreakable;
        this.deliveryDate = deliveryDate;
        this.deliveryPrice = deliveryPrice;
        this.vehicle = vehicle;
        this.endConsumerMobile = endConsumerMobile;
        this.currentOrderLocationInfo = currentOrderLocationInfo;
        this.packageState = packageState;
        this.verifiedUser = verifiedUser;
        this.endConsumerName = endConsumerName;
        this.merchantMobile = merchantMobile;
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

    public LocationInfo getCurrentOrderLocationInfo() {
        return currentOrderLocationInfo;
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
                ", currentOrderLocationInfo=" + currentOrderLocationInfo +
                ", packageName='" + packageName + '\'' +
                ", packageImageURL='" + packageImageURL + '\'' +
                ", packageDescription='" + packageDescription + '\'' +
                ", packagePrice='" + packagePrice + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", deliveryPrice='" + deliveryPrice + '\'' +
                ", prePaid=" + prePaid +
                ", breakable=" + breakable +
                ", verifiedUser=" + verifiedUser +
                ", delivered=" + delivered +
                ", picked=" + picked +
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

    public boolean isDelivered() {
        return delivered;
    }

    public boolean isPicked() {
        return picked;
    }
}
