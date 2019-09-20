package com.example.zagelx.Models;

public class FreeDelegateDetails {

    private int noOfOrders;
    private String currentLat;
    private String currentLng;
    private int commissionCashToBeCollected;
    private int ordersCashToBeCollected;
    public FreeDelegateDetails() {
    }

    public FreeDelegateDetails(int noOfOrders, String currentLat, String currentLng, int commissionCashToBeCollected, int ordersCashToBeCollected) {
        this.noOfOrders = noOfOrders;
        this.currentLat = currentLat;
        this.currentLng = currentLng;
        this.commissionCashToBeCollected = commissionCashToBeCollected;
        this.ordersCashToBeCollected = ordersCashToBeCollected;
    }

    public int getNoOfOrders() {
        return noOfOrders;
    }

    public String getCurrentLat() {
        return currentLat;
    }

    public String getCurrentLng() {
        return currentLng;
    }

    public int getCommissionCashToBeCollected() {
        return commissionCashToBeCollected;
    }

    public int getOrdersCashToBeCollected() {
        return ordersCashToBeCollected;
    }


    @Override
    public String toString() {
        return "FreeDelegateDetails{" +
                "noOfOrders=" + noOfOrders +
                ", currentLat='" + currentLat + '\'' +
                ", currentLng='" + currentLng + '\'' +
                ", commissionCashToBeCollected=" + commissionCashToBeCollected +
                ", ordersCashToBeCollected=" + ordersCashToBeCollected +
                '}';
    }
}
