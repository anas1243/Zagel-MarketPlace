package com.example.zagelx.Models;

public class Users {
    private String ID = "";
    private String name = "";
    private String email = "";
    private String mode = ""; //delegate:d, merchant: m
    private String availabilityStatus = ""; //online:on, offline:off
    private String gender = ""; //male:male, female:female
    private String noOFOrders = "";
    private String noOfTrips = "";
    private String mobileNumber = "";
    private String profilePictureURL = "";
    private String nationalIdURL = "";
    private String electricityReceiptURL = "";
    private int rate = 0;
    private boolean firstTimeLogIn = true;
    private boolean verified = false;
    private boolean accurateLocation = false;
    //date with format year month day
    private BirthDate birthDate = null;
    private MerchantsNotifications Notifications = null;
    private int numberOfNotifications=0;
    private String userToken;
    private LocationInfoForUsers locationInfoForUser = null;


    public Users() {
    }

    public Users(String ID, String name, String gender, String mobileNumber,
                 String profilePictureURL, BirthDate birthDate, String mode,
                 String email, LocationInfoForUsers locationInfoForUser, boolean firstTimeLogIn, boolean verified, boolean accurateLocation,
                 String userToken) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.profilePictureURL = profilePictureURL;
        this.firstTimeLogIn = firstTimeLogIn;
        this.ID = ID;
        this.birthDate = birthDate;
        this.firstTimeLogIn = firstTimeLogIn;
        this.mode = mode;
        this.verified = verified;
        this.accurateLocation = accurateLocation;
        this.userToken = userToken;
        this.locationInfoForUser = locationInfoForUser;
    }

    public LocationInfoForUsers getLocationInfoForUser() {
        return locationInfoForUser;
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

    public String getMobileNumber() {
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

    public int getRate() {
        return rate;
    }

    public boolean isFirstTimeLogIn() {
        return firstTimeLogIn;
    }

    public String getID() {
        return ID;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public boolean isVerified() {
        return verified;
    }

    public MerchantsNotifications getNotifications() {
        return Notifications;
    }

    public int getNumberOfNotifications() {
        return numberOfNotifications;
    }

    public String getUserToken() {
        return userToken;
    }

    public boolean isAccurateLocation() {
        return accurateLocation;
    }
}
