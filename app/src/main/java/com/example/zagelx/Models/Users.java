package com.example.zagelx.Models;

public class Users {
    private String ID = "";
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
    private boolean firstTimeLogIn = true;


    public Users() {
    }

    public Users(String ID, String name, String gender, int mobileNumber,
                 String profilePictureURL, boolean firstTimeLogIn) {
        this.name = name;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.profilePictureURL = profilePictureURL;
        this.firstTimeLogIn = firstTimeLogIn;
        this.ID = ID;
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

    public boolean isFirstTimeLogIn() {
        return firstTimeLogIn;
    }

    public String getID() {
        return ID;
    }
}
