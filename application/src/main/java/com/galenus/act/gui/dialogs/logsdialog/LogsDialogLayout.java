package com.galenus.act.gui.dialogs.logsdialog;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.Application;
import com.galenus.act.gui.components.IDialog;

import javax.swing.*;
import java.awt.*;

import static com.galenus.act.Application.imageResource;

abstract class LogsDialogLayout extends IDialog {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JTabbedPane tabbedPane = new JTabbedPane();
    private SettingsPanel settingsPanel;
    private SerialLogsPanel serialLogsPanel;
    private WebLogsPanel webLogsPanel;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    LogsDialogLayout(Application application, String title) {
        super(application, title);
        setResizable(true);
        setModal(false);
    }


    /*
     *                  METHODS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    void updateSerialTableData() {
        serialLogsPanel.updateTableData();
    }

    void updateWebTableData() {
        webLogsPanel.updateTableData();
    }



    /*
     *                  LISTENERS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void initializeComponents() {
        // Title
        setTitleIcon(imageResource.readImage("Serial.Log.Title"));
        setTitleName(getTitle());

        // Panels
        settingsPanel = new SettingsPanel();
        serialLogsPanel = new SerialLogsPanel();
        webLogsPanel = new WebLogsPanel();

    }

    @Override
    public void initializeLayouts() {
        getContentPanel().setLayout(new BorderLayout());

        tabbedPane.addTab("Settings", settingsPanel);
        tabbedPane.addTab("Serial", serialLogsPanel);
        tabbedPane.addTab("Web", webLogsPanel);

        getContentPanel().add(tabbedPane);

        pack();
    }

    @Override
    public void updateComponents(Object... object) {
        // Serial port
        if (object.length > 0 && object[0] != null) {
            serialLogsPanel.updateComponents((SerialPort) object[0]);
        } else {
            serialLogsPanel.updateComponents();
        }
        // Web
        webLogsPanel.updateComponents();
        // Settings
        settingsPanel.updateComponents();

        // Tables
        updateSerialTableData();
        updateWebTableData();
    }
}
