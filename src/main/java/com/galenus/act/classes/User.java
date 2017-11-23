package com.galenus.act.classes;

import org.ksoap2.serialization.SoapObject;

public class User extends BaseClass {

    private String firstName;
    private String lastName;
    private String encodedPassword;
    private String encodedPin;
    private String code;
    // bitmap photo
    private int language;
    private int sex;

    public User() {

    }

    public User(SoapObject soapObject) {
        onSoapInit(soapObject);
    }

    @Override
    protected void onSoapInit(SoapObject soapObject) {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getEncodedPin() {
        return encodedPin;
    }

    public void setEncodedPin(String encodedPin) {
        this.encodedPin = encodedPin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
