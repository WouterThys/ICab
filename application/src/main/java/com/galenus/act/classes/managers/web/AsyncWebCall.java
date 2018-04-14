package com.galenus.act.classes.managers.web;

import com.galenus.act.Application;
import com.galenus.act.utils.DateUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import javax.swing.*;
import java.util.Date;
import java.util.Vector;

import static com.galenus.act.classes.managers.web.WebManager.webMgr;

public abstract class AsyncWebCall extends SwingWorker<AsyncWebResult<Object>, Void> {

    private Application application;
    private String methodName;
    private boolean success;
    private Date date;
    private Exception exception;

    AsyncWebCall(Application application, String methodName) {
        this.application = application;
        this.methodName = methodName;
    }

    abstract void onAddProperties(SoapObject soapRequest);

    private SoapSerializationEnvelope getRequest(SoapObject soapRequest) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;

        // Add mappings if needed
        // ex: soapEnvelope.addMapping(NAMESPACE, "AjsJob", Classes.DEVICEJOB_CLASS);

        envelope.setOutputSoapObject(soapRequest);
        return envelope;
    }

    @Override
    protected AsyncWebResult<Object> doInBackground() throws Exception {
        // Set variables
        AsyncWebResult<Object> result = null;
        success = true;
        date = DateUtils.now();
        webMgr().addWebCallToList(AsyncWebCall.this);

        //Start
        application.startWait(application);
        try {
            SoapObject soapRequest = new SoapObject(webMgr().getWebNameSpace(), methodName);
            onAddProperties(soapRequest);

            SoapSerializationEnvelope envelope = getRequest(soapRequest);
            HttpTransportSE httpTransport = new HttpTransportSE(webMgr().getWebUrl(), webMgr().getWebTimeout());

            httpTransport.call(webMgr().getWebNameSpace() + methodName, envelope);
            result = new AsyncWebResult<>(envelope.getResponse());
        } catch (Exception ex) {
            this.exception = ex;
            webMgr().onFailedRequest(methodName, ex, 0);
            success = false;
        } finally {
            application.stopWait(application);
        }
        return result;
    }

    @Override
    protected void done() {
        if (success) {
            try {
                AsyncWebResult<Object> result = get();
                if (result.getResult() instanceof SoapPrimitive) {
                     Vector<Object> res = new Vector<>();
                     res.add(result.getResult());
                     webMgr().onFinishedRequest(methodName, res);
                } else {
                    webMgr().onFinishedRequest(methodName, (Vector) result.getResult());
                }
            } catch (Exception ex) {
                this.exception = ex;
                webMgr().onFailedRequest(methodName, ex, 0);
            }
        }
    }

    public String getMethodName() {
        if (methodName == null) {
            methodName = "";
        }
        return methodName;
    }

    public boolean isSuccess() {
        return success;
    }

    public Date getDate() {
        if (date == null) {
            date = DateUtils.minDate();
        }
        return date;
    }
}
