package com.galenus.act.web;

import com.galenus.act.gui.Application;
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

    public static final String WebCall_DeviceRegister = "RegisterDevice";
    public static final String WebCall_DeviceUnRegister = "UnRegisterDevice";
    public static final String WebCall_DeviceGetUsers = "GetDeviceUsers";

    private Application application;

    private String deviceName;
    private int deviceType;

    private String webUrl;
    private String webNameSpace;
    private int webTimeout;


    private List<WebCallListener> webCallListenerList = new ArrayList<>();


    public void init(Application application, String deviceName, String webUrl, String webNameSpace, int webTimeout) {
        this.application = application;

        this.deviceName = deviceName;
        this.deviceType = 1;

        this.webUrl = webUrl;
        this.webNameSpace = webNameSpace;
        this.webTimeout = webTimeout;

        this.webCallListenerList = new ArrayList<>();
    }

    public void close() {

    }

    public void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    public void addOnWebCallListener(WebCallListener webCallListener) {
        if (!webCallListenerList.contains(webCallListener)) {
            webCallListenerList.add(webCallListener);
        }
    }

    public void removeOnWebCallListener(WebCallListener webCallListener) {
        if (webCallListenerList.contains(webCallListener)) {
            webCallListenerList.remove(webCallListener);
        }
    }

    /*
     * Package private methods
     */
    void onFinishedRequest(String methodName, Vector response) {
        for (WebCallListener listener : webCallListenerList) {
            listener.onFinishedRequest(methodName, response);
        }
    }
    void onFailedRequest(String methodName, Exception ex, int fault) {
        for (WebCallListener listener : webCallListenerList) {
            listener.onFailedRequest(methodName, ex, fault);
        }
    }


    /*
     * Web calls
     */

    public void registerDevice() {
        new AsyncWebCall(application, WebCall_DeviceRegister) {
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

    public void getDeviceUsers() {
        new AsyncWebCall(application, WebCall_DeviceGetUsers) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
            }
        }.execute();
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
