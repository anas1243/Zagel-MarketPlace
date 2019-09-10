package com.example.zagelx.Models;

import java.io.Serializable;

public class RequestInfo implements Serializable {
    private String requestId;
    private String userID;
    private String userName;
    private String userImageURL;
    private String userMobile;
    private int rating;
    private String offerPrice;
    private boolean verified;
    private String status; //pending, accepted or rejected request;

    public RequestInfo() {
    }

    public String getStatus() {
        return status;
    }

    public String getRequestId() {
        return requestId;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isVerified() {
        return verified;
    }

    public RequestInfo(String requestId, String userID, String userName, String userImageURL
            , String userMobile, int rating, String offerPrice, boolean verified, String status) {
        this.userID = userID;
        this.userName = userName;
        this.userImageURL = userImageURL;
        this.rating = rating;
        this.offerPrice = offerPrice;
        this.verified = verified;
        this.requestId = requestId;
        this.userMobile = userMobile;
        this.status = status;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserMobile() {
        return userMobile;
    }


}
