package com.galenus.act.utils;

import com.galenus.act.classes.managers.serial.SerialMessage;

import static com.galenus.act.classes.managers.serial.MessageFactory.*;

public class StringValues {

    public static String picCommandToString(String command) {
        if (command != null && !command.isEmpty()) {
            switch (command) {
                case PIC_INIT:
                    return "Initialize";
                case PIC_RESET:
                    return "Reset";
                case PIC_LOCK:
                    return "Lock";
                case PIC_UNLOCK:
                    return "Unlock";
                case PIC_ERROR:
                    return "Error";
                case PIC_ALARM:
                    return "Alarm";
                case PIC_STATE:
                    return "State";
            }

            if (command.contains("D")) {
                return command.replace("D", "Door ");
            }
            return command;
        }
        return "/";
    }

    public static String picMessageToString(SerialMessage serialMessage) {
        if (serialMessage != null) {
            String message = serialMessage.getMessage();
            String command = serialMessage.getCommand();
            if (message != null && command != null) {
                if (command.contains(PIC_DOOR)) {
                    switch (message) {
                        case "O":
                            return "Open";
                        case "C":
                            return "Closed";
                    }
                } else if (command.contains(PIC_ALARM)) {
                    switch (message) {
                        case "0":
                            return "Off";
                        case "1":
                            return "Soft";
                        case "2":
                            return "Hard";
                    }
                } else if (command.contains(PIC_STATE)) {
                    switch (message) {
                        case "1":
                            return "Running";
                        case "0":
                            return "Waiting for init";
                    }
                }
                return message;
            }
        }
        return "/";
    }
}
