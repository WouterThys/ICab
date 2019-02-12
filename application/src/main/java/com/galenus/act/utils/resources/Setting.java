package com.galenus.act.utils.resources;

public class Setting<T> {

    public interface SettingsConnector<S> {
        S readSetting();
        void writeSetting(S s);
    }

    private T value;
    private boolean isRead;
    private boolean shouldRestart;
    private final SettingsConnector<T> connector;

    Setting(boolean shouldRestart, SettingsConnector<T> connector) {
        this.shouldRestart = shouldRestart;
        this.connector = connector;
        this.isRead = false;
    }

    public T getValue() {
        if (!isRead) {
            value = connector.readSetting();
            isRead = true;
        }
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isShouldRestart() {
        return shouldRestart;
    }

    public void setShouldRestart(boolean shouldRestart) {
        this.shouldRestart = shouldRestart;
    }
}
