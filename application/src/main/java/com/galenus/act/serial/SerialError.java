package com.galenus.act.serial;

public class SerialError {

    public enum ErrorType {
        OpenError,
        WriteError,
        ReadError,
        OtherError
    }

    private SerialMessage serialMessage;
    private ErrorType errorType;
    private String message;
    private Throwable throwable;

    public SerialError(ErrorType errorType, SerialMessage serialMessage, Throwable throwable, String message) {
        this.errorType = errorType;
        this.serialMessage = serialMessage;
        this.throwable = throwable;
        this.message = message;
    }

    public SerialError(ErrorType errorType, Throwable throwable, String message) {
        this(errorType, null, throwable, message);
    }

    public SerialError(ErrorType errorType, String message) {
        this(errorType, null, message);
    }

    public SerialMessage getSerialMessage() {
        return serialMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
