package com.example.zagelx.Models;

import java.io.Serializable;
//model class for storing notification info that comes to the merchants

public class MerchantsNotifications implements Serializable {
    private String notificationId;
    private String type;
    private String purpose;   //OnWay, Picked, Delivered
    private String merchantId;
    private String orderName;
    private String orderId;
    private String whichBranch;
    private CourierInfo courierInfo;

    public MerchantsNotifications(String notificationId, String type, String purpose
            , String merchantId, String orderName, String orderId
            , String whichBranch, CourierInfo courierInfo) {
        this.orderName = orderName;
        this.orderId = orderId;
        this.courierInfo = courierInfo;
        this.merchantId = merchantId;
        this.notificationId = notificationId;
        this.type = type;
        this.purpose = purpose;
        this.whichBranch = whichBranch;
    }

    public MerchantsNotifications() {
    }

    public String getOrderName() {
        return orderName;
    }

    public String getOrderId() {
        return orderId;
    }

    public CourierInfo getCourierInfo() {
        return courierInfo;
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

    public String getWhichBranch() {
        return whichBranch;
    }
}
