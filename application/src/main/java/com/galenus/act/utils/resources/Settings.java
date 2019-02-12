package com.galenus.act.utils.resources;

import java.util.Set;

public class Settings extends BasicResource {

    private static final String DM = "DEBUG_MODE";
    private static final String FS = "FULL_SCREEN";
    private static final String DC = "DOOR_COUNT";
    private static final String PD = "PING_DELAY";
    private static final String ULT = "USER_LOGON_TIME";
    private static final String WURL = "WEB_URL";
    private static final String INM = "NAME";
    private static final String TFS = "TAB_FONT_SIZE";

    private static final Settings instance = new Settings();
    public static Settings getSettings() {
        return instance;
    }
    private Settings() {
        super();
    }

    private Setting<Boolean> isDebugMode = new Setting<>(true, new Setting.SettingsConnector<Boolean>() {
        @Override
        public Boolean readSetting() {
            return Boolean.valueOf(readProperty(DM));
        }

        @Override
        public void writeSetting(Boolean aBoolean) {
            writeBoolean(DM, aBoolean);
        }
    });
    private Setting<Boolean> isFullScreen = new Setting<>(true, new Setting.SettingsConnector<Boolean>() {
        @Override
        public Boolean readSetting() {
            return Boolean.valueOf(readProperty(FS));
        }

        @Override
        public void writeSetting(Boolean aBoolean) {
            writeBoolean(FS, aBoolean);
        }
    });
    private Setting<Integer> doorCount = new Setting<>(true, new Setting.SettingsConnector<Integer>() {
        @Override
        public Integer readSetting() {
            return Integer.valueOf(readProperty(DC));
        }

        @Override
        public void writeSetting(Integer integer) {
            writeInt(DC, integer);
        }
    });
    private Setting<Integer> logonTime = new Setting<>(false, new Setting.SettingsConnector<Integer>() {
        @Override
        public Integer readSetting() {
            return Integer.valueOf(readProperty(ULT));
        }

        @Override
        public void writeSetting(Integer integer) {
            writeInt(ULT, integer);
        }
    });
    private Setting<Integer> pingDelay = new Setting<>(false, new Setting.SettingsConnector<Integer>() {
        @Override
        public Integer readSetting() {
            return Integer.valueOf(readProperty(PD));
        }

        @Override
        public void writeSetting(Integer integer) {
            writeInt(PD, integer);
        }
    });
    private Setting<Integer> tabFontSize = new Setting<>(true, new Setting.SettingsConnector<Integer>() {
        @Override
        public Integer readSetting() {
            return Integer.valueOf(readProperty(TFS));
        }

        @Override
        public void writeSetting(Integer integer) {
            writeInt(TFS, integer);
        }
    });
    private Setting<String> webUrl = new Setting<>(true, new Setting.SettingsConnector<String>() {
        @Override
        public String readSetting() {
            return readProperty(WURL);
        }

        @Override
        public void writeSetting(String s) {
            writeString(WURL, s);
        }
    });
    private Setting<String> name = new Setting<>(true, new Setting.SettingsConnector<String>() {
        @Override
        public String readSetting() {
            return readProperty(INM);
        }

        @Override
        public void writeSetting(String s) {
            writeString(INM, s);
        }
    });


    @Override
    public void initialize(String propertiesUrl, String fileName) {
        super.initialize(propertiesUrl, fileName);

    }

    public boolean isDebugMode() {
        return isDebugMode.getValue();
    }

    public boolean isFullScreen() {
        return isFullScreen.getValue();
    }

    public String getWebUrl() {
        return webUrl.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    public int getDoorCount() {
        return doorCount.getValue();
    }

    public int getUserLogonTime() {
        return logonTime.getValue();
    }

    public int getPingDelay() {
        return pingDelay.getValue();
    }

    public int getTabFontSize() {
        return tabFontSize.getValue();
    }
}
