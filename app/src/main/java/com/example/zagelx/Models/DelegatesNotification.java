package com.example.zagelx.Models;

import java.io.Serializable;
//model class for storing notification info that comes to the delegates
public class DelegatesNotification implements Serializable {
    private String tripId;
    private RequestInfo requestInfo;

    public DelegatesNotification(String tripId, RequestInfo requestInfo) {
        this.tripId = tripId;
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
}
