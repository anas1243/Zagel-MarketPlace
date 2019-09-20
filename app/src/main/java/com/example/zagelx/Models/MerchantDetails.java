package com.example.zagelx.Models;

public class MerchantDetails {

    private String howMayOrdersInDay;
    private String businessIndustry;

    private int noOFOrdersInAlex = 0;
    private int noOFOrdersInCairo = 0;

    private int noOfOrdersFromAlexToCairo = 0;
    private int noOfOrdersFromCairoToAlex = 0;

    private int cashToGive = 0;

    public MerchantDetails() {
    }


    public String getHowMayOrdersInDay() {
        return howMayOrdersInDay;
    }

    public String getBusinessIndustry() {
        return businessIndustry;
    }

    public MerchantDetails( String howMayOrdersInDay
            , String businessIndustry, int noOFOrdersInAlex
            , int noOFOrdersInCairo, int noOfOrdersFromAlexToCairo, int noOfOrdersFromCairoToAlex, int cashToGive) {
        this.howMayOrdersInDay = howMayOrdersInDay;
        this.businessIndustry = businessIndustry;

        this.noOFOrdersInAlex = noOFOrdersInAlex;
        this.noOFOrdersInCairo = noOFOrdersInCairo;
        this.noOfOrdersFromAlexToCairo = noOfOrdersFromAlexToCairo;
        this.noOfOrdersFromCairoToAlex = noOfOrdersFromCairoToAlex;
        this.cashToGive = cashToGive;

    }

    public int getNoOFOrdersInAlex() {
        return noOFOrdersInAlex;
    }

    public int getNoOFOrdersInCairo() {
        return noOFOrdersInCairo;
    }

    public int getNoOfOrdersFromAlexToCairo() {
        return noOfOrdersFromAlexToCairo;
    }

    public int getNoOfOrdersFromCairoToAlex() {
        return noOfOrdersFromCairoToAlex;
    }

    public int getCashToGive() {
        return cashToGive;
    }


    @Override
    public String toString() {
        return "MerchantDetails{" +
                "howMayOrdersInDay='" + howMayOrdersInDay + '\'' +
                ", businessIndustry='" + businessIndustry + '\'' +
                ", noOFOrdersInAlex=" + noOFOrdersInAlex +
                ", noOFOrdersInCairo=" + noOFOrdersInCairo +
                ", noOfOrdersFromAlexToCairo=" + noOfOrdersFromAlexToCairo +
                ", noOfOrdersFromCairoToAlex=" + noOfOrdersFromCairoToAlex +
                ", cashToGive=" + cashToGive +
                '}';
    }
}
