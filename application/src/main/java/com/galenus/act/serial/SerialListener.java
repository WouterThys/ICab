package com.galenus.act.serial;

import com.fazecast.jSerialComm.SerialPort;

public interface SerialListener {
    void onInitSuccess(SerialPort serialPort);
    void onSerialError(String error);
    // TODO other
    void onNewMessage(SerialMessage message);
}
