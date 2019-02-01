package com.galenus.act.gui.models;

import com.galenus.act.classes.managers.serial.SerialMessage;
import com.galenus.act.utils.DateUtils;
import com.galenus.act.utils.StringValues;

import javax.swing.*;

import static com.galenus.act.Application.imageResource;

public class SerialLogTableModel extends IAbstractTableModel<SerialMessage> {

    private static final String[] COLUMN_NAMES = {"", "Date", "Value", "Command", "Message", "Ack"};
    private static final Class[] COLUMN_CLASSES = {ImageIcon.class, String.class, String.class, String.class, String.class, ImageIcon.class};

    private static final ImageIcon ackImage = imageResource.readImage("Serial.Log.Ack");
    private static final ImageIcon nAckImage = imageResource.readImage("Serial.Log.NAck");
    private static final ImageIcon okImage = imageResource.readImage("Serial.Log.Ok");
    private static final ImageIcon txImage = imageResource.readImage("Serial.Log.Tx");
    private static final ImageIcon rxImage = imageResource.readImage("Serial.Log.Rx");

    public SerialLogTableModel() {
        super(COLUMN_NAMES, COLUMN_CLASSES);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SerialMessage message = getItemAt(rowIndex);

        if (message != null) {
            switch (columnIndex) {
                case -1:
                    return message;
                case 0: // Acknowledged
                    if (message.getId() >= 0) {
                        return txImage;
                    } else {
                        return rxImage;
                    }
                case 1: // Date
                    return DateUtils.formatTime(message.getDate());
                case 2: // Value
                    return message.toString();
                case 3: // Command
                    return StringValues.picCommandToString(message.getCommand());
                case 4: // Message
                    return StringValues.picMessageToString(message);
                case 5: // Ack
                    if (message.isAcknowledged()) {
                        return ackImage;
                    } else {
                        return nAckImage;
                    }
            }
        }
        return null;
    }

}
