package com.example.zagelx.Models;

import java.io.Serializable;

public class CourierInfo implements Serializable {
    private String requestId;
    private String userID;
    private String userName;
    private String userImageURL;
    private String userMobile;
    private String userGroup;
    private int rating;
    private boolean verified;

    public CourierInfo() {
    }

    public String getRequestId() {
        return requestId;
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

    public CourierInfo(String requestId, String userID, String userName, String userImageURL
            , String userMobile, String userGroup, int rating, boolean verified) {
        this.userID = userID;
        this.userName = userName;
        this.userImageURL = userImageURL;
        this.rating = rating;
        this.verified = verified;
        this.requestId = requestId;
        this.userMobile = userMobile;
        this.userGroup = userGroup;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public String getUserGroup() {
        return userGroup;
    }
}
