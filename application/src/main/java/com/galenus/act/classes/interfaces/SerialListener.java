package com.galenus.act.classes.interfaces;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.serial.SerialMessage;

public interface SerialListener {
    void onInitSuccess(SerialPort serialPort);
    void onSerialError(String error);
    // TODO other
    void onNewMessage(SerialMessage message);
}
