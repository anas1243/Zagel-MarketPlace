package com.example.zagelx.Models;

public class Users {
    private String ID = "";
    private String name = "";
    private String email = "";
    private String mode = ""; //delegate:d, merchant: m
    private String group = ""; //group that identifies users' actions and notifications to send
    private String availabilityStatus = ""; //online:on, offline:off
    private String gender = ""; //male:male, female:female
    private String mobileNumber = "";
    private String profilePictureURL = "";
    private String nationalIdURL = "";
    private String electricityReceiptURL = "";
    private int rate = 0;
    private boolean firstTimeLogIn = true;
    private boolean verified = false;  //if delegates are not verified don't show the app
    private boolean accurateLocation = false;
    //date with format year month day
    private BirthDate birthDate = null;
    private int numberOfNotifications=0;
    private String userToken;
    private LocationInfoForUsers locationInfoForUser = null;
    private MerchantDetails merchantDetails = null;
    private FreeDelegateDetails freeDelegateDetails = null;
    private PMDetails pmDetails = null;


    public Users() {
    }

    public void setPmDetails(PMDetails pmDetails) {
        this.pmDetails = pmDetails;
    }

    public PMDetails getPmDetails() {
        return pmDetails;
    }

    public MerchantDetails getMerchantDetails() {
        return merchantDetails;
    }

    public FreeDelegateDetails getFreeDelegateDetails() {
        return freeDelegateDetails;
    }

    public Users(String ID, String name, String gender, String mobileNumber,
                 String profilePictureURL, BirthDate birthDate, String mode, String group,
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
        this.group = group;
        this.verified = verified;
        this.accurateLocation = accurateLocation;
        this.userToken = userToken;
        this.locationInfoForUser = locationInfoForUser;
    }

    public void setMerchantDetails(MerchantDetails merchantDetails) {
        this.merchantDetails = merchantDetails;
    }

    public void setFreeDelegateDetails(FreeDelegateDetails freeDelegateDetails) {
        this.freeDelegateDetails = freeDelegateDetails;
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

    public int getNumberOfNotifications() {
        return numberOfNotifications;
    }

    public String getUserToken() {
        return userToken;
    }

    public boolean isAccurateLocation() {
        return accurateLocation;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "Users{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mode='" + mode + '\'' +
                ", group='" + group + '\'' +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                ", gender='" + gender + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", profilePictureURL='" + profilePictureURL + '\'' +
                ", nationalIdURL='" + nationalIdURL + '\'' +
                ", electricityReceiptURL='" + electricityReceiptURL + '\'' +
                ", rate=" + rate +
                ", firstTimeLogIn=" + firstTimeLogIn +
                ", verified=" + verified +
                ", accurateLocation=" + accurateLocation +
                ", birthDate=" + birthDate +
                ", numberOfNotifications=" + numberOfNotifications +
                ", userToken='" + userToken + '\'' +
                ", locationInfoForUser=" + locationInfoForUser +
                ", merchantDetails=" + merchantDetails +
                ", freeDelegateDetails=" + freeDelegateDetails +
                ", pmDetails=" + pmDetails +
                '}';
    }
}
