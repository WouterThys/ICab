package com.galenus.act.classes.interfaces;

import com.fazecast.jSerialComm.SerialPort;
import com.galenus.act.classes.managers.serial.SerialError;
import com.galenus.act.classes.managers.serial.SerialMessage;

public interface SerialListener {
    void onInitSuccess(SerialPort serialPort);
    void onSerialError(SerialError serialError);
    void onNewRead(SerialMessage message);
    void onNewWrite(SerialMessage message);
}
