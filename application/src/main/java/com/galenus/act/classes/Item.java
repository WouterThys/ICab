package com.galenus.act.classes;

import org.ksoap2.serialization.SoapObject;

public class Item extends BaseClass {

    private int amount;
    private String description;
    private String location;

    public Item() {

    }

    @Override
    public String toString() {
        return "Item: " + getCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Item) && super.equals(obj);
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        if (description == null) {
            description = "";
        }
        return description;
    }

    public String getLocation() {
        if (location == null) {
            location = "";
        }
        return location;
    }

    @Override
    protected void onSoapInit(SoapObject soapObject) {

    }
}
