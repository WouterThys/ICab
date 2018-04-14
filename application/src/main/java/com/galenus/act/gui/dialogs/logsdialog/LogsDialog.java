package com.galenus.act.gui.dialogs.logsdialog;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.Application;
import com.galenus.act.classes.interfaces.SerialListener;
import com.galenus.act.classes.interfaces.WebCallListener;
import com.galenus.act.classes.managers.serial.SerialError;
import com.galenus.act.classes.managers.serial.SerialManager;
import com.galenus.act.classes.managers.serial.SerialMessage;
import com.galenus.act.classes.managers.web.WebManager;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class LogsDialog extends LogsDialogLayout implements SerialListener, WebCallListener {


    public LogsDialog(Application application, String title, SerialPort serialPort) {
        super(application, title);

        initializeComponents();
        initializeLayouts();
        updateComponents(serialPort);

        SerialManager.serMgr().addSerialListener(this);
        WebManager.webMgr().addOnWebCallListener(this);
    }


    //
    // Dialog
    //
    @Override
    public void windowClosed(WindowEvent e) {
        SerialManager.serMgr().removeSerialListener(this);
        super.windowClosed(e);
    }

    //
    // Serial listener
    //
    @Override
    public void onInitSuccess(SerialPort serialPort) {
        updateComponents(serialPort);
    }

    @Override
    public void onSerialError(SerialError error) {
        JOptionPane.showMessageDialog(
                LogsDialog.this,
                error,
                "Serial error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void onNewWrite(SerialMessage message) {
        SwingUtilities.invokeLater(this::updateSerialTableData);
    }

    @Override
    public void onNewRead(SerialMessage message) {
        SwingUtilities.invokeLater(this::updateSerialTableData);
    }

    //
    // Web listener
    //
    @Override
    public void onFinishedRequest(String methodName, Vector response) {
        updateWebTableData();
    }

    @Override
    public void onFailedRequest(String methodName, Exception ex, int fault) {
        updateWebTableData();
    }
}
