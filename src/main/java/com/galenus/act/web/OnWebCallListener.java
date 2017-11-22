package com.galenus.act.web;

import java.util.Vector;

public interface OnWebCallListener {
    void FinishedRequest(String methodName);
    void SetResponse(String methodName, Vector response);
    void FailedRequest(String methodName, Exception ex, int fault);
}
