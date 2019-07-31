package com.example.zagelx.Models;

import java.io.Serializable;
//model class for storing notification info that comes to the merchants

public class MerchantsNotifications implements Serializable {
    private String notificationId;
    private String type;
    private String purpose;
    private String merchantId;
    private String orderName;
    private String orderId;
    private RequestInfo requestInfo;

    private String tripId;
    private String tripDate;

    public MerchantsNotifications(String notificationId, String type, String purpose
            , String merchantId, String orderName, String orderId
            , RequestInfo requestInfo, String tripId, String tripDate) {
        this.orderName = orderName;
        this.orderId = orderId;
        this.requestInfo = requestInfo;
        this.merchantId = merchantId;
        this.notificationId = notificationId;
        this.type = type;
        this.purpose = purpose;
        this.tripId = tripId;
        this.tripDate = tripDate;
    }

    public MerchantsNotifications() {
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderId() {
        return orderId;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getType() {
        return type;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getTripId() {
        return tripId;
    }

    public String getTripDate() {
        return tripDate;
    }
}
