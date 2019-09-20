package com.example.zagelx.Models;

public class PMDetails {
    private int commissionCashToBeCollected;
    private int ordersCashToBeCollected;

    public PMDetails(int commissionCashToBeCollected, int ordersCashToBeCollected) {
        this.commissionCashToBeCollected = commissionCashToBeCollected;
        this.ordersCashToBeCollected = ordersCashToBeCollected;
    }

    public int getCommissionCashToBeCollected() {
        return commissionCashToBeCollected;
    }

    public int getOrdersCashToBeCollected() {
        return ordersCashToBeCollected;
    }

    public PMDetails() {
    }

    @Override
    public String toString() {
        return "PMDetails{" +
                "commissionCashToBeCollected=" + commissionCashToBeCollected +
                ", ordersCashToBeCollected=" + ordersCashToBeCollected +
                '}';
    }
}
