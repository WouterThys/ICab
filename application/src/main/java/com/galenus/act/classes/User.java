package com.galenus.act.classes;

import com.galenus.act.utils.SoapUtils;
import org.ksoap2.serialization.SoapObject;

import javax.swing.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.galenus.act.gui.Application.imageResource;

public class User extends BaseClass {

    public enum UserLanguage {
        Dutch,
        French,
        English;

        public static int enumToInt(UserLanguage userLanguage) {
            switch (userLanguage) {
                case Dutch: return 0;
                case French: return 1;
                case English: return 2;
                default: return -1;
            }
        }

        public static UserLanguage intToEnum(int userLanguage) {
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

        public static int enumToInt(UserSex userSex) {
            switch (userSex) {
                case Machine: return 0;
                case Female: return 1;
                case Male: return 2;
                default: return -1;
            }
        }

        public static UserSex intToEnum(int userSex) {
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

    public User() {
        language = UserLanguage.French;
        sex = UserSex.Female;
        avatar = null;
    }

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
            sex = UserSex.intToEnum(SoapUtils.convertToInt(soapObject, "UserSex"));
            language = UserLanguage.intToEnum(SoapUtils.convertToInt(soapObject, "Lan"));

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public UserLanguage getLanguage() {
        return language;
    }

    public void setLanguage(UserLanguage language) {
        this.language = language;
    }

    public UserSex getSex() {
        return sex;
    }

    public void setSex(UserSex sex) {
        this.sex = sex;
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

}
