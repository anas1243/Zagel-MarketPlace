package com.example.zagelx.Models;

import java.io.Serializable;
//model class for storing notification info that comes to the merchants

public class MerchantsNotifications implements Serializable {
    private String orderName;
    private String orderId;
    private RequestInfo requestInfo;

    public MerchantsNotifications(String orderName, String orderId, RequestInfo requestInfo) {
        this.orderName = orderName;
        this.orderId = orderId;
        this.requestInfo = requestInfo;
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
}
