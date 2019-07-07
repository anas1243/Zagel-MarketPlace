package com.example.zagelx.Models;

import java.io.Serializable;

public class RequestInfo implements Serializable {
    private String delegateID;
    private String delegateName;
    private String delegateImageURL;
    private int rating;
    private String offerPrice;
    private boolean verified;

    public RequestInfo() {
    }



    public String getOfferPrice() {
        return offerPrice;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public String getDelegateImageURL() {
        return delegateImageURL;
    }

    public int getRating() {
        return rating;
    }

    public String getDelegateID() {
        return delegateID;
    }

    public boolean isVerified() {
        return verified;
    }

    public RequestInfo(String delegateID, String delegateName, String delegateImageURL
            , int rating, String offerPrice, boolean verified) {
        this.delegateID = delegateID;
        this.delegateName = delegateName;
        this.delegateImageURL = delegateImageURL;
        this.rating = rating;
        this.offerPrice = offerPrice;
        this.verified = verified;
    }
}
