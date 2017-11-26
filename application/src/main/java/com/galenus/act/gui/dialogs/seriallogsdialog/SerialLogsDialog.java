package com.galenus.act.gui.dialogs.seriallogsdialog;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.gui.Application;
import com.galenus.act.serial.SerialListener;
import com.galenus.act.serial.SerialManager;
import com.galenus.act.serial.SerialMessage;

import javax.swing.*;
import java.awt.event.WindowEvent;

import static com.galenus.act.serial.SerialManager.serMgr;

public class SerialLogsDialog extends SerialLogsDialogLayout implements SerialListener{


    public SerialLogsDialog(Application application, String title, SerialPort serialPort) {
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
    // Test
    //
    @Override
    void onPicInit() {
        serMgr().sendInit();
    }

    @Override
    void onPicReset() {
        serMgr().sendReset();
    }

    @Override
    void onPicLock() {
        serMgr().sendLockAll();
    }

    @Override
    void onPicUnlock() {
        serMgr().sendUnlockAll();
    }

    @Override
    void onPicError() {

    }

    //
    // Serial listener
    //
    @Override
    public void onInitSuccess(SerialPort serialPort) {
        //
    }

    @Override
    public void onSerialError(String error) {
        //
    }

    @Override
    public void onNewMessage(SerialMessage message) {
        SwingUtilities.invokeLater(this::setTableData);
    }
}
