package com.galenus.act.classes;

import com.galenus.act.utils.DateUtils;
import com.galenus.act.utils.SoapUtils;
import org.apache.commons.lang3.StringUtils;
import org.ksoap2.serialization.SoapObject;

import javax.swing.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static com.galenus.act.Application.imageResource;

public class User extends BaseClass {

    public enum UserLanguage {
        Dutch,
        French,
        English;

        public static UserLanguage fromInt(int userLanguage) {
            switch (userLanguage) {
                case 0: return Dutch;
                case 1: return French;
                case 2: return English;
                default: return Dutch;
            }
        }
    }

    public enum UserSex {
        Machine,
        Female,
        Male;

        public static UserSex fromInt(int userSex) {
            switch (userSex) {
                case 0: return Machine;
                case 1: return Female;
                case 2: return Male;
                default: return Female;
            }
        }
    }

    private String firstName;
    private String lastName;
    private String encodedPassword;
    private String encodedPin;
    private ImageIcon avatar;
    private UserLanguage language;
    private UserSex sex;
    private boolean canStopTimer;

    private boolean isLoggedIn = false;
    private boolean isOverTime = false;
    private Date lastLogIn = DateUtils.minDate();
    private int loggedInTime;

    public User(SoapObject soapObject) {
        onSoapInit(soapObject);
    }

    @Override
    protected void onSoapInit(SoapObject soapObject) {
        try {
            code = SoapUtils.convertToString(soapObject, "Code");
            firstName = SoapUtils.convertToString(soapObject, "FirstName");
            lastName = SoapUtils.convertToString(soapObject, "LastName");
            encodedPassword = SoapUtils.convertToString(soapObject, "EncodedPassword");
            encodedPin = SoapUtils.convertToString(soapObject, "EncodedPIN");
            sex = UserSex.fromInt(SoapUtils.convertToInt(soapObject, "UserSex"));
            language = UserLanguage.fromInt(SoapUtils.convertToInt(soapObject, "Lan"));
            avatar = SoapUtils.convertToImageIcon(soapObject, "Avatar");
            canStopTimer = SoapUtils.convertToBool(soapObject, "CanStopTimer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void copyFrom(BaseClass baseClass) {
        if (baseClass != null && baseClass instanceof User) {
            User from = (User) baseClass;
            firstName = from.getFirstName();
            lastName = from.getLastName();
            encodedPassword = from.getEncodedPassword();
            encodedPin = from.getEncodedPin();
            avatar = from.getAvatar();
            language = from.getLanguage();
            sex = from.getSex();
            canStopTimer = from.isCanStopTimer();
        }
    }

    public void logIn() {
        this.isLoggedIn = true;
        this.loggedInTime = 0;
        this.isOverTime = false;
        this.lastLogIn = DateUtils.now();
    }

    public void logOut() {
        this.isLoggedIn = false;
        this.isOverTime = false;
        this.loggedInTime = 0;
    }

    public boolean isPinCorrect(String pin) {
        String encoded = getEncryptedString(pin);
        return encoded.equals(getEncodedPin());
    }

    public static String getEncryptedString(String original) {
        String encrypted;

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(original.getBytes(),0,original.length());

            encrypted = new BigInteger(1,md5.digest()).toString(16);
            StringBuilder str = new StringBuilder(encrypted);

            if (encrypted.length() < 32) {
                encrypted = StringUtils.leftPad(encrypted, 32, '0');
            }

            int i = 2;
            int length = encrypted.length();
            while (i < length) {
                str.insert(i,'-');
                length++;
                i += 3;
            }
            encrypted = str.toString().toUpperCase();

        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        return encrypted;
    }

    public String getLoggedInTimeString(int maxTime) {
        int timeRemaining = maxTime - loggedInTime;
        int minutes = 0;
        boolean minusSign = timeRemaining < 0;
        int seconds = Math.abs(timeRemaining);

        while (seconds >= 60) {
            minutes++;
            seconds -= 60;
        }

        String result = minusSign ? "-" : "";

        return result + String.valueOf(minutes) + ":" + String.format("%02d", seconds);
    }


    public String getFirstName() {
        if (firstName == null) {
            firstName = "";
        }
        if (!firstName.isEmpty()) {
            firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        }
        return firstName;
    }

    public String getLastName() {
        if (lastName == null) {
            lastName = "";
        }
        return lastName;
    }

    public String getEncodedPassword() {
        if (encodedPassword == null) {
            encodedPassword = "";
        }
        return encodedPassword;
    }

    public String getEncodedPin() {
        if (encodedPin == null) {
            encodedPin = "";
        }
        return encodedPin;
    }

    public UserLanguage getLanguage() {
        if (language == null) {
            language = UserLanguage.French;
        }
        return language;
    }

    public UserSex getSex() {
        if (sex == null) {
            sex = UserSex.Female;
        }
        return sex;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isOverTime() {
        if (!isLoggedIn) {
            isOverTime = false;
        }
        return isOverTime;
    }

    public void setOverTime(boolean overTime) {
        isOverTime = overTime;
    }

    public Date getLastLogIn() {
        return lastLogIn;
    }

    public ImageIcon getAvatar() {
        if (avatar == null) {
            switch (sex) {
                default:
                case Female: avatar = imageResource.readImage("Users.Sex.Female"); break;
                case Male: avatar = imageResource.readImage("Users.Sex.Male"); break;
                case Machine: avatar = imageResource.readImage("Users.Sex.Machine"); break;
            }
        }
        return avatar;
    }

    public int getLoggedInTime() {
        return loggedInTime;
    }

    public void setLoggedInTime(int loggedInTime) {
        this.loggedInTime = loggedInTime;
    }

    public boolean isCanStopTimer() {
        return canStopTimer;
    }
}
