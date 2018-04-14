package com.galenus.act.classes;

import com.galenus.act.utils.SoapUtils;
import org.ksoap2.serialization.SoapObject;

public class Item extends BaseClass {

    private int amount;
    private String description;
    private String location;

    public Item() {
    }

    public Item(SoapObject soapObject) {
        onSoapInit(soapObject);
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
        try {
            code = SoapUtils.convertToString(soapObject, "ItemCode");
            amount = SoapUtils.convertToInt(soapObject, "Dotation");
            description = SoapUtils.convertToString(soapObject, "ItemDesc0");
            location = SoapUtils.convertToString(soapObject, "Location");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
