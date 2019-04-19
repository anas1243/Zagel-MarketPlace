package com.example.zagelx.Models;

public class Orders {
    private int userImage = 0;//we be removed
    private String merchantId = "";
    private String acceptedDelegate;
    private String packageState;
    private String endConcumerMobile;

    private int packageImage = 0;
    private String packageName = "";
    private String deliveryDate = "";
    private String deliveryPrice = "";

    private String source = "";
    private String destination = "";
    private int vehicleImage = 0;

    public Orders() {
    }

    public Orders(int userImage, String merchantId, int packageImage,
                  String packageName, String delvieryDate, String deliveryPrice,
                  String source, String destination, int vehicleImage) {
        this.userImage = userImage;
        this.merchantId = merchantId;
        this.packageImage = packageImage;
        this.packageName = packageName;
        this.deliveryDate = delvieryDate;
        this.deliveryPrice = deliveryPrice;
        this.source = source;
        this.destination = destination;
        this.vehicleImage = vehicleImage;
    }

    public int getUserImage() {
        return userImage;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public int getPackageImage() {
        return packageImage;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getvehicleImage() {
        return vehicleImage;
    }
}
