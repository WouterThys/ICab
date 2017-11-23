package com.galenus.act.web;

class AsyncWebResult<T> {

    private T result;
    private Exception exception;

    AsyncWebResult(T result) {
        this.result = result;
    }

    AsyncWebResult(Exception exception, String methodName) {
        this.exception = exception;
    }

    public T getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }


}
