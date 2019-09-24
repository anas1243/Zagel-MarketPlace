package com.example.zagelx.Models;

import java.io.Serializable;
import java.util.Map;

public class Trips implements Serializable {
    private String tripId = "";
    private String delegateImageURL = "";
    private String delegateID = "";
    private String PMID = "";
    private String delegateName = "";

    private BirthDate routeDate = null;
    private String routePrice = "";
    private String routeDescription = "";
    private boolean verifiedUser = false;
    private boolean fullRoute = false;
    //is it done?
    private boolean status = false;
    private boolean passedToOtherSide = false;

    private int numberOfRequests = 0;

    private String vehicle = "";

    private LocationInfoForPackage locationInfoForTrip = null;

    private int maxNoOrders ;
    private Map<String, Map<String, String>> routeOrders = null;

    private boolean prePaid = false;
    private boolean breakable = false;
    private String maxPrePaidLimit = "";

    public Trips() {
    }

    public Trips(String tripId, String PMID, String delegateImageURL, String delegateID
            , String delegateName, BirthDate routeDate, String routePrice
            , String routeDescription, String vehicle, LocationInfoForPackage locationInfoForTrip
            , int maxNoOrders, boolean prePaid, boolean breakable, String maxPrePaidLimit
            , boolean verifiedUser, boolean status, boolean passedToOtherSide) {
        this.tripId = tripId;
        this.delegateImageURL = delegateImageURL;
        this.delegateID = delegateID;
        this.delegateName = delegateName;
        this.routeDate = routeDate;
        this.routePrice = routePrice;
        this.routeDescription = routeDescription;
        this.vehicle = vehicle;
        this.locationInfoForTrip = locationInfoForTrip;
        this.maxNoOrders = maxNoOrders;
        this.prePaid = prePaid;
        this.breakable = breakable;
        this.maxPrePaidLimit = maxPrePaidLimit;
        this.verifiedUser = verifiedUser;
        this.PMID = PMID;
        this.status = status;
        this.passedToOtherSide = passedToOtherSide;
    }

    public String getTripId() {
        return tripId;
    }

    public String getDelegateImageURL() {
        return delegateImageURL;
    }

    public String getDelegateID() {
        return delegateID;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public BirthDate getRouteDate() {
        return routeDate;
    }

    public String getRoutePrice() {
        return routePrice;
    }

    public String getRouteDescription() {
        return routeDescription;
    }

    public String getVehicle() {
        return vehicle;
    }

    public LocationInfoForPackage getLocationInfoForTrip() {
        return locationInfoForTrip;
    }

    public Map<String, Map<String, String>> getRouteOrders() {
        return routeOrders;
    }

    public int getMaxNoOrders() {
        return maxNoOrders;
    }

    public boolean isPrePaid() {
        return prePaid;
    }

    public String getMaxPrePaidLimit() {
        return maxPrePaidLimit;
    }

    public boolean isBreakable() {
        return breakable;
    }

    public boolean isVerifiedUser() {
        return verifiedUser;
    }

    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    public boolean isFullRoute() {
        return fullRoute;
    }

    public String getPMID() {
        return PMID;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isPassedToOtherSide() {
        return passedToOtherSide;
    }
}
