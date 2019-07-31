package com.example.zagelx.Models;

import java.io.Serializable;
//model class for storing notification info that comes to the delegates
public class DelegatesNotification implements Serializable {
    private String notificationsId;
    private String type;
    private String purpose;
    private String delegateId;
    private String tripId;
    private String orderName;
    private String orderId;
    private String tripDate;
    private RequestInfo requestInfo;

    public DelegatesNotification(String notificationsId, String type, String purpose, String delegateId, String tripId, String tripDate
            , String orderId, String orderName, RequestInfo requestInfo) {
        this.tripId = tripId;
        this.orderId = orderId;
        this.tripDate = tripDate;
        this.orderName = orderName;
        this.requestInfo = requestInfo;
        this.delegateId = delegateId;
        this.notificationsId = notificationsId;
        this.type = type;
        this.purpose = purpose;
    }

    public DelegatesNotification() {
    }

    public String getTripId() {
        return tripId;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getTripDate() {
        return tripDate;
    }

    public String getDelegateId() {
        return delegateId;
    }

    public String getNotificationsId() {
        return notificationsId;
    }

    public String getType() {
        return type;
    }

    public String getPurpose() {
        return purpose;
    }
}
