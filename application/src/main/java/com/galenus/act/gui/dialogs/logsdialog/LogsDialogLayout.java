package com.galenus.act.gui.dialogs.logsdialog;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.gui.Application;
import com.galenus.act.gui.components.IDialog;

import javax.swing.*;
import java.awt.*;

import static com.galenus.act.gui.Application.imageResource;

abstract class LogsDialogLayout extends IDialog {

    /*
     *                  COMPONENTS
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private JTabbedPane tabbedPane = new JTabbedPane();
    private SerialLogsPanel serialLogsPanel;
    private WebLogsPanel webLogsPanel;

    /*
     *                  CONSTRUCTOR
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    LogsDialogLayout(Application application, String title) {
        super(application, title);
        setResizable(true);
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
        serialLogsPanel = new SerialLogsPanel();
        webLogsPanel = new WebLogsPanel();

    }

    @Override
    public void initializeLayouts() {
        getContentPanel().setLayout(new BorderLayout());

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

        // Tables
        updateSerialTableData();
        updateWebTableData();
    }
}
