package com.example.zagelx.Models;

import java.io.Serializable;

public class PmsNotifications implements Serializable {
    private String notificationId;
    private String type;
    private String purpose;   //OnWay, Picked, HeadQuarters, OtherHeadQuarters, Delivered
    private String merchantId;
    private String merchantName;
    private String delegateId;
    private String delegateName;
    private String orderName;
    private String orderId;
    private String endConsumerName;
    private String whichBranch;
    private CourierInfo courierInfo;

    public PmsNotifications(String notificationId, String type, String purpose
            , String merchantId, String merchantName
            , String delegateId, String delegateName, String endConsumerName, String orderName, String orderId
            , String whichBranch, CourierInfo courierInfo) {
        this.orderName = orderName;
        this.orderId = orderId;
        this.courierInfo = courierInfo;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.delegateId = delegateId;
        this.delegateName = delegateName;
        this.endConsumerName = endConsumerName;
        this.notificationId = notificationId;
        this.type = type;
        this.purpose = purpose;
        this.whichBranch = whichBranch;
    }

    public PmsNotifications() {
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getDelegateId() {
        return delegateId;
    }

    public String getDelegateName() {
        return delegateName;
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

    public String getEndConsumerName() {
        return endConsumerName;
    }
}
