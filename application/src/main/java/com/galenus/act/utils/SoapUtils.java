package com.galenus.act.utils;

import org.ksoap2.serialization.SoapObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Date;

public class SoapUtils {

    /**
     *                          Convert from soap objects
     * *********************************************************************************************
     */

    public static String convertToString(SoapObject object, String name) {
        if (object != null && object.hasProperty(name)) {
            String value = object.getProperty(name).toString();
            if (value.contains("anyType")) {
                return "";
            }
            else return value;
        } else {
            if (object != null) {
                System.err.println("Unknown object name: '"+name+"' in "+object.getPropertySafely("ObjectName").toString());
            } else {
                System.err.println("Null object passed in convertToString(...)");
            }
        }
        return "";
    }

    public static ImageIcon convertToImageIcon(SoapObject object, String name) {
        if (object != null && object.hasProperty(name)) {
            String value = object.getProperty(name).toString();
            if (value.contains("anyType")) {
                return null;
            }

            byte[] decodedString = null;
            try {
                decodedString = Base64.getDecoder().decode(value.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (decodedString != null) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(decodedString));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (img != null) {
                    return new ImageIcon(img);
                }
            }
        } else {
            if (object != null) {
                System.err.println("Unknown object name: '"+name+"' in "+object.getPropertySafely("ObjectName").toString());
            } else {
                System.err.println("Null object passed in convertToString(...)");
            }
        }
        return null;
    }

    public static long convertToLong(SoapObject object, String name) {
        if (object != null && object.hasProperty(name)) {
            String value = object.getProperty(name).toString();
            if (value.contains("anyType")) {
                System.err.println("Convert to string error: anyType");
                return -1;
            }
            else return Long.valueOf(value);
        } else {
            if (object != null) {
                System.err.println("Unknown object name: '"+name+"' in "+object.getPropertySafely("ObjectName").toString());
            } else {
                System.err.println("Null object passed in convertToString(...)");
            }
        }
        return -1;
    }

    public static int convertToInt(SoapObject object, String name) {
        if (object != null && object.hasProperty(name)) {
            String value = object.getProperty(name).toString();
            if (value.contains("anyType")) {
                System.err.println("Convert to string error: anyType");
                return -1;
            }
            else return Integer.valueOf(value);
        } else {
            if (object != null) {
                System.err.println("Unknown object name: '"+name+"' in "+object.getPropertySafely("ObjectName").toString());
            } else {
                System.err.println("Null object passed in convertToString(...)");
            }
        }
        return -1;
    }

    public static double convertToDouble(SoapObject object, String name) {
        if (object != null && object.hasProperty(name)) {
            String value = object.getProperty(name).toString();
            if (value.contains("anyType")) {
                System.err.println("Convert to string error: anyType");
                return -1;
            }
            else return Double.valueOf(value);
        } else {
            if (object != null) {
                System.err.println("Unknown object name: '"+name+"' in "+object.getPropertySafely("ObjectName").toString());
            } else {
                System.err.println("Null object passed in convertToString(...)");
            }
        }
        return -1;
    }

    public static boolean convertToBool(SoapObject object, String name) {
        if (object != null && object.hasProperty(name)) {
            String value = object.getProperty(name).toString();
            if (value.contains("anyType")) {
                System.err.println("Convert to string error: anyType");
                return false;
            }
            else return Boolean.valueOf(value);
        } else {
            if (object != null) {
                System.err.println("Unknown object name: '"+name+"' in "+object.getPropertySafely("ObjectName").toString());
            } else {
                System.err.println("Null object passed in convertToString(...)");
            }
        }
        return false;
    }

    public static Date convertToDate(SoapObject object, String name) {
        if (object != null && object.hasProperty(name)) {
            String value = object.getProperty(name).toString();
            if (value.contains("anyType")) {
                System.err.println("Convert to string error: anyType");
                return DateUtils.minDate();
            }
            else {
                return DateUtils.convertServerDate(value);
            }
        } else {
            if (object != null) {
                System.err.println("Unknown object name: '"+name+"' in "+object.getPropertySafely("ObjectName").toString());
            } else {
                System.err.println("Null object passed in convertToString(...)");
            }
        }
        return DateUtils.minDate();
    }

}
