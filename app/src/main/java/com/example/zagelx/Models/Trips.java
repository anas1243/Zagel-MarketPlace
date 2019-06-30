package com.example.zagelx.Models;

import java.io.Serializable;
import java.util.Map;

public class Trips implements Serializable {

    private String delegateImageURL = "";
    private String delegateID = "";
    private String delegateName = "";

    private BirthDate routeDate = null;
    private String routePrice = "";
    private String routeDescription = "";

    private String vehicle = "";

    private LocationInfo currentOrderLocationInfo = null;

    private String maxNoOrders = "";
    private Map<String, String> routeOrders ;

    private boolean prePaid = false;
    private boolean breakable = false;
    private String maxPrePaidLimit = "";

    public Trips() {
    }

    public Trips(String delegateImageURL, String delegateID
            , String delegateName, BirthDate routeDate, String routePrice
            , String routeDescription, String vehicle, LocationInfo currentOrderLocationInfo
            , String maxNoOrders, boolean prePaid, boolean breakable, String maxPrePaidLimit) {
        this.delegateImageURL = delegateImageURL;
        this.delegateID = delegateID;
        this.delegateName = delegateName;
        this.routeDate = routeDate;
        this.routePrice = routePrice;
        this.routeDescription = routeDescription;
        this.vehicle = vehicle;
        this.currentOrderLocationInfo = currentOrderLocationInfo;
        this.maxNoOrders = maxNoOrders;
        this.prePaid = prePaid;
        this.breakable = breakable;
        this.maxPrePaidLimit = maxPrePaidLimit;
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

    public LocationInfo getCurrentOrderLocationInfo() {
        return currentOrderLocationInfo;
    }

    public Map<String, String> getRouteOrders() {
        return routeOrders;
    }

    public String getMaxNoOrders() {
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
}
