package com.galenus.act.gui.dialogs.logsdialog;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.gui.Application;
import com.galenus.act.classes.interfaces.SerialListener;
import com.galenus.act.serial.SerialManager;
import com.galenus.act.serial.SerialMessage;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class LogsDialog extends LogsDialogLayout implements SerialListener{


    public LogsDialog(Application application, String title, SerialPort serialPort) {
        super(application, title);

        initializeComponents();
        initializeLayouts();
        updateComponents(serialPort);

        SerialManager.serMgr().addSerialListener(this);
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
    public void onSerialError(String error) {
        JOptionPane.showMessageDialog(
                LogsDialog.this,
                error,
                "OpenWhileLocked",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void onNewMessage(SerialMessage message) {
        SwingUtilities.invokeLater(this::updateSerialTableData);
    }
}
