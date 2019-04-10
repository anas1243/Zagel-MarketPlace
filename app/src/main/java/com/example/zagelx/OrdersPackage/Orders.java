package com.example.zagelx.OrdersPackage;

public class Orders {
    private int userImage;
    private String userName;

    private int packageImage;
    private String packageName;
    private String deliveryDate;
    private String deliveryPrice;

    private String source;
    private String destination;
    private int vehcileImage;

    public Orders() {
    }

    public Orders(int userImage, String userName, int packageImage,
                  String packageName, String delvieryDate, String deliveryPrice,
                  String source, String destination, int vehcileImage) {
        this.userImage = userImage;
        this.userName = userName;
        this.packageImage = packageImage;
        this.packageName = packageName;
        this.deliveryDate = delvieryDate;
        this.deliveryPrice = deliveryPrice;
        this.source = source;
        this.destination = destination;
        this.vehcileImage = vehcileImage;
    }

    public int getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
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

    public int getVehcileImage() {
        return vehcileImage;
    }
}
