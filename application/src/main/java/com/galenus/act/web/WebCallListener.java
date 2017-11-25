package com.galenus.act.web;

import java.util.Vector;

public interface WebCallListener {
    void onFinishedRequest(String methodName, Vector response);
    void onFailedRequest(String methodName, Exception ex, int fault);
}
