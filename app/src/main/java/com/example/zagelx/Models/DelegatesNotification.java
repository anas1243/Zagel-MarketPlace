package com.example.zagelx.Models;

import java.io.Serializable;
//model class for storing notification info that comes to the delegates
public class DelegatesNotification implements Serializable {
    private String notificationsId;
    private String type;
    private String purpose;
    private String delegateId;
    private String orderName;
    private String orderId;
    private String whichBranch;
    private CourierInfo courierInfo;

    public DelegatesNotification(String notificationsId, String type, String purpose, String delegateId
            , String orderId, String orderName, String whichBranch, CourierInfo courierInfo) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.courierInfo = courierInfo;
        this.delegateId = delegateId;
        this.notificationsId = notificationsId;
        this.type = type;
        this.purpose = purpose;
        this.whichBranch = whichBranch;
    }

    public DelegatesNotification() {
    }

    public CourierInfo getCourierInfo() {
        return courierInfo;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderId() {
        return orderId;
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

    public String getWhichBranch() {
        return whichBranch;
    }
}
