package com.example.zagelx.Models;

import java.io.Serializable;
//model class for storing notification info that comes to the delegates
public class DelegatesNotification implements Serializable {
    private String tripId;
    private String orderName;
    private String orderId;
    private String tripDate;
    private RequestInfo requestInfo;

    public DelegatesNotification(String tripId, String tripDate, String orderId, String orderName, RequestInfo requestInfo) {
        this.tripId = tripId;
        this.orderId = orderId;
        this.tripDate = tripDate;
        this.orderName = orderName;
        this.requestInfo = requestInfo;
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
}
