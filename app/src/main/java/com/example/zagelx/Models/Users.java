package com.example.zagelx.Models;

public class Users {
    private String name = "";
    private String mode = ""; //delegate:d, merchant: m
    private String availabilityStatus = ""; //online:on, offline:off
    private String gender = ""; //male:male, female:female
    private String noOFOrders = "";
    private String noOfTrips = "";
    private int mobileNumber = 0;
    private String profilePictureURL = "";
    private String nationalIdURL = "";
    private String electricityReceiptURL = "";
    private String government = ""; // living government
    private int rate = 0;

    public Users(){}

    public Users(String name, String mode, String availabilityStatus,
                 String gender, String noOFOrders, String noOfTrips, int mobileNumber,
                 String profilePictureURL, String nationalIdURL, String electricityReceiptURL,
                 String government, int rate) {
        this.name = name;
        this.mode = mode;
        this.availabilityStatus = availabilityStatus;
        this.gender = gender;
        this.noOFOrders = noOFOrders;
        this.noOfTrips = noOfTrips;
        this.mobileNumber = mobileNumber;
        this.profilePictureURL = profilePictureURL;
        this.nationalIdURL = nationalIdURL;
        this.electricityReceiptURL = electricityReceiptURL;
        this.government = government;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public String getMode() {
        return mode;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public String getGender() {
        return gender;
    }

    public String getNoOFOrders() {
        return noOFOrders;
    }

    public String getNoOfTrips() {
        return noOfTrips;
    }

    public int getMobileNumber() {
        return mobileNumber;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getNationalIdURL() {
        return nationalIdURL;
    }

    public String getElectricityReceiptURL() {
        return electricityReceiptURL;
    }

    public String getGovernment() {
        return government;
    }

    public int getRate() {
        return rate;
    }
}
