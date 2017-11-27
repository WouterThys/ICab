package com.galenus.act.gui.dialogs.seriallogsdialog;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.Main;
import com.galenus.act.gui.Application;
import com.galenus.act.classes.interfaces.SerialListener;
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
    // Actions
    //
    @Override
    void onRetry() {
        //application.initializeSerial(SerialLogsDialog.this, serMgr().getMainSerialListener());
    }

    @Override
    void onDeleteRx() {
        int res = JOptionPane.showConfirmDialog(
                SerialLogsDialog.this,
                "Delete all received messages?",
                "Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (res == JOptionPane.YES_OPTION) {
            serMgr().clearRxMessages();
            updateTableData();
        }
    }

    @Override
    void onDeleteTx() {
        int res = JOptionPane.showConfirmDialog(
                SerialLogsDialog.this,
                "Delete all send messages?",
                "Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (res == JOptionPane.YES_OPTION) {
            serMgr().clearTxMessages();
            updateTableData();
        }
    }

    //
    // Test
    //
    @Override
    void onPicInit() {
        serMgr().sendInit(Main.DOOR_COUNT);
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
        updateComponents(serialPort);
    }

    @Override
    public void onSerialError(String error) {
        JOptionPane.showMessageDialog(
                SerialLogsDialog.this,
                error,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void onNewMessage(SerialMessage message) {
        SwingUtilities.invokeLater(this::updateTableData);
    }
}
