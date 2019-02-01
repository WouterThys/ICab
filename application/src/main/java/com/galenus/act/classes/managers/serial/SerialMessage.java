package com.galenus.act.classes.managers.serial;

import com.galenus.act.utils.DateUtils;

import java.util.Date;

public class SerialMessage {

    public static final String StartChar = "&";
    public static final String StopChar = "$";
    public static final String Separator = ":";
    public static final String Message = "[M]";
    public static final String Acknowledge = "[A]";

    private int id;
    private String sender;
    private String type;
    private String command;
    private String message;
    private Date date; // Send/Receive date
    private SerialMessage ackMessage;

    private static int ackId;

    public SerialMessage(String type, String command) {
        this(type, command, "");
    }

    public SerialMessage(String type, String command, String message) {
        this(createId(), type, command, message);
    }

    public SerialMessage(int id, String type, String command, String message) {
        this(id, "", type, command, message);
    }

    public SerialMessage(int id, String sender, String type, String command, String message) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.command = command;
        this.message = message;
        this.ackMessage = null;
        this.date = DateUtils.now();
    }

    private static int createId() {
        ackId++;
        if (ackId > 9) {
            ackId = 0;
        }
        return ackId;
    }

    //&[M]::L::2$

    @Override
    public String toString() {
        if (getType().equals(Acknowledge)) {
            return StartChar +
                    Acknowledge +
                    getId() +
                    StopChar;
        } else {
            if (getId() >= 0) {
                return StartChar +
                        getType() + Separator +
                        getSender() + Separator +
                        getCommand() + Separator +
                        getMessage() + Separator +
                        getId() +
                        StopChar;
            } else {
                return StartChar +
                        getType() +
                        getSender() + Separator +
                        getCommand() + Separator +
                        getMessage() +
                        StopChar;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SerialMessage) {
            SerialMessage message = (SerialMessage) obj;
            return (message.getId() == getId()) &&
                    (message.getType().equals(getType())) &&
                    (message.getCommand().equals(getCommand())) &&
                    (message.getMessage().equals(getMessage())) &&
                    (message.isAcknowledged() == isAcknowledged());
        }
        return false;
    }

    public boolean incoming() {
        return getId() == -1;
    }

    public String getSender() {
        if (sender == null) {
            sender = "";
        }
        return sender;
    }

    public String getType() {
        if (type == null) {
            type = "";
        }
        return type;
    }

    public String getCommand() {
        if (command == null) {
            command = "";
        }
        return command;
    }

    public String getMessage() {
        if (message == null) {
            message = "";
        }
        return message;
    }

    public void setAcknowledged(SerialMessage ack) {
        this.ackMessage = ack;
    }

    public boolean isAcknowledged() {
        return (ackMessage != null) && (ackMessage.getId() == getId());
    }

    public SerialMessage getAckMessage() {
        return ackMessage;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }
}
