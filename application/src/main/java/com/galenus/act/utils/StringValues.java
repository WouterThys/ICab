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
            }

            if (command.contains("P")) {
                return command.replace("P", "Port ");
            }
        }
        return "/";
    }

    public static String picMessageToString(String message) {
        if (message != null && !message.isEmpty()) {
            switch (message) {
                case "O": return "Open";
                case "C": return "Closed";
            }
        }
        return "/";
    }
}
