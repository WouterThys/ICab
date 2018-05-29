package com.galenus.act.classes.managers.web;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.WebCallListener;
import com.galenus.act.Application;
import com.galenus.act.utils.DateUtils;
import org.ksoap2.serialization.SoapObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WebManager {

    private static WebManager Instance = new WebManager();
    public static WebManager webMgr() {
        return Instance;
    }
    private WebManager() {}

    public static final String WebCall_Ping = "PingData";
    public static final String WebCall_Register = "RegisterDevice";
    public static final String WebCall_UnRegister = "UnRegisterDevice";
    public static final String WebCall_GetUsers = "GetDeviceUsers";
    public static final String WebCall_GetItems = "GetTypeItems";
    public static final String WebCall_LogOn = "SignalLogOn";
    public static final String WebCall_LogOff = "SignalLogOff";
    public static final String WebCall_DoorOpen = "SignalDoorOpen";
    public static final String WebCall_DoorClose = "SignalDoorClose";
    public static final String WebCall_AlarmDoorNotClosed = "SignalAlarmDoorNotClosed";
    public static final String WebCall_AlarmDoorForced = "SignalAlarmDoorForced";
    public static final String WebCall_StopTimer = "SignalStopTimer";
    public static final String WebCall_StartTimer = "SignalStartTimer";

    private Application application;
    private List<WebCallListener> webCallListenerList = new ArrayList<>();
    private List<AsyncWebCall> webCallList = new ArrayList<>();

    private String deviceName;
    private int deviceType;

    private String webUrl;
    private String webNameSpace;
    private int webTimeout;
    private boolean webSuccess;

    private PingThread pingThread;


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
        try {
            if (pingThread != null) {
                pingThread.stop();
            }
        } catch (Exception e) {
            // Nothing we can do..
        }
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

    public void startPinging(int delayInMillis) {
        if (pingThread != null) {
            pingThread.cancel(true);
        }

        pingThread = new PingThread(delayInMillis);
        pingThread.execute();
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

    void addWebCallToList(AsyncWebCall webCall) {
        if (!webCallList.contains(webCall)) {
            webCallList.add(webCall);
        }
    }

    public List<AsyncWebCall> getWebCallList() {
        return webCallList;
    }

    public void clearWebCallList() {
        webCallList.clear();
    }


    /*
     * Web calls
     */

    public void ping() {
        new AsyncWebCall(application, WebCall_Ping) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aDTMostRecentInventory", DateUtils.convertToServerDate(DateUtils.now()));
            }
        }.execute();
    }

    public void registerDevice() {
        new AsyncWebCall(application, WebCall_Register) {
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
        new AsyncWebCall(application, WebCall_GetUsers) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
            }
        }.execute();
    }

    public void logOn(User user) {
        new AsyncWebCall(application, WebCall_LogOn) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aUser", user.getCode());
                soapRequest.addProperty("aTimeStamp", DateUtils.convertToServerDate(user.getLastLogIn()));
            }
        }.execute();
    }

    public void logOff(User user) {
        new AsyncWebCall(application, WebCall_LogOff) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aUserCode", user.getCode());
                soapRequest.addProperty("aLoggedOffHow", 0); //??
                soapRequest.addProperty("aTimeStamp", DateUtils.convertToServerDate(DateUtils.now()));
            }
        }.execute();
    }

    public void getDeviceItems() {
        new AsyncWebCall(application, WebCall_GetItems) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
            }
        }.execute();
    }

    public void doorOpen(User user) {
        new AsyncWebCall(application, WebCall_DoorOpen) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aUser", user.getCode());
                soapRequest.addProperty("aTimeStamp", DateUtils.convertToServerDate(DateUtils.now()));
            }
        }.execute();
    }

    public void doorClose() {
        new AsyncWebCall(application, WebCall_DoorClose) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aTimeStamp", DateUtils.convertToServerDate(DateUtils.now()));
            }
        }.execute();
    }

    public void alarmDoorNotClosed() {
        new AsyncWebCall(application, WebCall_AlarmDoorNotClosed) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aTimeStamp", DateUtils.convertToServerDate(DateUtils.now()));
            }
        }.execute();
    }

    public void alarmDoorForced() {
        new AsyncWebCall(application, WebCall_AlarmDoorForced) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aTimeStamp", DateUtils.convertToServerDate(DateUtils.now()));
            }
        }.execute();
    }

    public void stoppedTimer(User user) {
        new AsyncWebCall(application, WebCall_StopTimer) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aUser", user.getCode());
                soapRequest.addProperty("aTimeStamp", DateUtils.convertToServerDate(DateUtils.now()));
            }
        }.execute();
    }

    public void startedTimer(User user) {
        new AsyncWebCall(application, WebCall_StartTimer) {
            @Override
            void onAddProperties(SoapObject soapRequest) {
                soapRequest.addProperty("aDeviceName", getDeviceName());
                soapRequest.addProperty("aUser", user.getCode());
                soapRequest.addProperty("aTimeStamp", DateUtils.convertToServerDate(DateUtils.now()));
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

    public boolean isWebSuccess() {
        return webSuccess;
    }

    public void setWebSuccess(boolean webSuccess) {
        this.webSuccess = webSuccess;
    }

    private static class PingThread extends SwingWorker<Void, Void> {

        private boolean keepRunning = true;
        private boolean enabled = true;

        private int delay;

        public PingThread(int delay) {
            this.delay = delay;
            this.keepRunning = true;
            this.enabled = true;
        }

        void stop() {
            keepRunning = false;
        }

        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        protected Void doInBackground() throws Exception {

            while (keepRunning) {
                try {
                    webMgr().ping();
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
