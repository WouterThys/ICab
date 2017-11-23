package com.galenus.act.web;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WebManager {

    private static WebManager Instance = new WebManager();
    public static WebManager webMgr() {
        return Instance;
    }
    private WebManager() {}

    private String deviceName;
    private int deviceType;

    private String webUrl;
    private String webNameSpace;
    private int webTimeout;


    private List<OnWebCallListener> webCallListenerList = new ArrayList<>();


    public void init(String deviceName, String webUrl, String webNameSpace, int webTimeout) {
        this.deviceName = deviceName;
        this.deviceType = 1;

        this.webUrl = webUrl;
        this.webNameSpace = webNameSpace;
        this.webTimeout = webTimeout;

        this.webCallListenerList = new ArrayList<>();
    }

    public void addOnWebCallListener(OnWebCallListener webCallListener) {
        if (!webCallListenerList.contains(webCallListener)) {
            webCallListenerList.add(webCallListener);
        }
    }

    public void removeOnWebCallListener(OnWebCallListener webCallListener) {
        if (webCallListenerList.contains(webCallListener)) {
            webCallListenerList.remove(webCallListener);
        }
    }

    /*
     * Package private methods
     */
    void onFinishedRequest(String methodName, Vector response) {
        for (OnWebCallListener listener : webCallListenerList) {
            listener.onFinishedRequest(methodName, response);
        }
    }
    void onFailedRequest(String methodName, Exception ex, int fault) {
        for (OnWebCallListener listener : webCallListenerList) {
            listener.onFailedRequest(methodName, ex, fault);
        }
    }


    /*
     * Web calls
     */

    public void registerDevice() {
        new AsyncWebCall("RegisterDevice") {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aTypeDevice", getDeviceType());
                soapRequest.addProperty("aDeviceInfo", "");
            }
        }.execute();
    }

    public void unregisterDevice() {
    }


    /*
     * Getters and setters
     */

    public String getDeviceName() {
        return deviceName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getWebNameSpace() {
        return webNameSpace;
    }

    public int getWebTimeout() {
        return webTimeout;
    }
}
