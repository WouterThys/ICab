package com.galenus.act.web;

import javax.swing.*;

public class AsyncWebCall extends SwingWorker<Void, Void> {

    private OnWebCallListener webCallListener;
    private String methodName;

    AsyncWebCall(OnWebCallListener webCallListener, String methodName) {
        this.webCallListener = webCallListener;
        this.methodName = methodName;
    }

    @Override
    protected Void doInBackground() throws Exception {
        return null;
    }

    @Override
    protected void done() {
        super.done();
    }
}
