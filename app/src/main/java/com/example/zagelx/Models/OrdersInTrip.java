package com.example.zagelx.Models;

import java.io.Serializable;

public class OrdersInTrip implements Serializable {
    private String orderId = "";
    private String orderURL = "";
    private String orderName = "";

    public OrdersInTrip(String orderId, String orderURL, String orderName) {
        this.orderId = orderId;
        this.orderURL = orderURL;
        this.orderName = orderName;
    }


    public OrdersInTrip() {
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderURL() {
        return orderURL;
    }

    public String getOrderName() {
        return orderName;
    }

    @Override
    public String toString() {
        return "OrdersInTrip{" +
                "orderId='" + orderId + '\'' +
                ", orderURL='" + orderURL + '\'' +
                ", orderName='" + orderName + '\'' +
                '}';
    }
}
