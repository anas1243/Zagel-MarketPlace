package com.example.zagelx.Models;

import java.io.Serializable;

public class RequestInfo implements Serializable {
    private String userID;
    private String userName;
    private String userImageURL;
    private int rating;
    private String offerPrice;
    private boolean verified;

    public RequestInfo() {
    }



    public String getOfferPrice() {
        return offerPrice;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public int getRating() {
        return rating;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isVerified() {
        return verified;
    }

    public RequestInfo(String userID, String userName, String userImageURL
            , int rating, String offerPrice, boolean verified) {
        this.userID = userID;
        this.userName = userName;
        this.userImageURL = userImageURL;
        this.rating = rating;
        this.offerPrice = offerPrice;
        this.verified = verified;
    }
}
