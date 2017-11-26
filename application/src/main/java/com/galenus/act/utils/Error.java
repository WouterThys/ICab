package com.galenus.act.utils;

import javax.swing.*;
import java.awt.*;

import static com.galenus.act.gui.Application.imageResource;

public class Error {

    public static final int NO_ERROR = 0;
    public static final int INFO = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;

    private int errorType;
    private String message;

    public Error(int errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

    int getErrorType() {
        return errorType;
    }

    public String getMessage() {
        return message;
    }

    public Color getColor() {
        switch (errorType) {
            case ERROR:
                return Color.RED;
            case INFO:
                return Color.BLUE;
            case NO_ERROR:
                return Color.WHITE;
            case WARNING:
                return Color.ORANGE;
            default:
                throw new IllegalArgumentException("Not a valid error type");
        }
    }

    public ImageIcon getImage() {
        switch (errorType) {
            case ERROR:
                return imageResource.readImage("ErrorProvider.ErrorIcon");
            case INFO:
                return imageResource.readImage("ErrorProvider.InfoIcon");
            case NO_ERROR:
                return null;
            case WARNING:
                return imageResource.readImage("ErrorProvider.WarningIcon");
            default:
                throw new IllegalArgumentException("Not a valid error type");
        }
    }
}
