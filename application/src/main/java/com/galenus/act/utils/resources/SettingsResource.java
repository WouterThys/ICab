package com.galenus.act.utils.resources;

public class SettingsResource extends BasicResource {

    private static final String DM = "DEBUG_MODE";
    private static final String FS = "FULL_SCREEN";
    private static final String DC = "DOOR_COUNT";
    private static final String PD = "PING_DELAY";
    private static final String ULT = "USER_LOGON_TIME";
    private static final String WURL = "WEB_URL";
    private static final String INM = "NAME";

    public SettingsResource(String propertiesUrl, String fileName) {
        super(propertiesUrl, fileName);
    }

    public boolean isDebugMode() {
        boolean isDebugMode;
        try {
            isDebugMode = Boolean.valueOf(readProperty(DM));
        } catch (Exception e) {
            System.err.println("Failed to read debug mode: " + e);
            isDebugMode = false;
        }
        return isDebugMode;
    }

    public boolean isFullScreen() {
        boolean isFullScreen;
        try {
            isFullScreen = Boolean.valueOf(readProperty(FS));
        } catch (Exception e) {
            System.err.println("Failed to read full screen mode: " + e);
            isFullScreen = false;
        }
        return isFullScreen;
    }

    public String getWebUrl() {
        String url;
        try {
            url = readProperty(WURL);
        } catch (Exception e) {
            System.err.println("Failed to read web url: " + e);
            url = "";
        }
        return url;
    }

    public String getName() {
        String name;
        try {
            name = readProperty(INM);
        } catch (Exception e) {
            System.err.println("Failed to read name: " + e);
            name = "";
        }
        return name;
    }

    public int getDoorCount() {
        int doorCount;
        try {
            doorCount = Integer.valueOf(readProperty(DC));
        } catch (Exception e) {
            System.err.println("Failed to read door count: " + e);
            doorCount = 0;
        }
        return doorCount;
    }

    public int getUserLogonTime() {
        int logonTime;
        try {
            logonTime = Integer.valueOf(readProperty(ULT));
        } catch (Exception e) {
            System.err.println("Failed to read user logon time: " + e);
            logonTime = 0;
        }
        return logonTime;
    }

    public int getPingDelay() {
        int delay;
        try {
            delay = Integer.valueOf(readProperty(PD));
        } catch (Exception e) {
            System.err.println("Failed to read ping delay: " + e);
            delay = 0;
        }
        return delay;
    }
}
