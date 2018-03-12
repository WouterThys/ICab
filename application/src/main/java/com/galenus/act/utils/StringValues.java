package com.galenus.act.utils;

public class StringValues {

    public static String picCommandToString(String command) {
        if (command != null && !command.isEmpty()) {
            switch (command) {
                case "I":
                    return "Initialize";
                case "R":
                    return "Reset";
                case "L":
                    return "Lock";
                case "U":
                    return "Unlock";
                case "E":
                    return "Error";
                case "A":
                    return "Alarm";
            }

            if (command.contains("D")) {
                return command.replace("D", "Door ");
            }
            return command;
        }
        return "/";
    }

    public static String picMessageToString(String message) {
        if (message != null && !message.isEmpty()) {
            switch (message) {
                case "O": return "Open";
                case "C": return "Closed";
                case "0": return "Off";
                case "1": return "Soft";
                case "2": return "Hard";
            }
            return message;
        }
        return "/";
    }
}
