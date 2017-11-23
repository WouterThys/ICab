package com.galenus.act.classes;

import org.ksoap2.serialization.SoapObject;

public abstract class BaseClass {

    protected String code;

    public BaseClass() {

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BaseClass && getCode().equals(((BaseClass) obj).getCode());
    }

    public String getCode() {
        if (code == null) {
            code = "";
        }
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    protected abstract void onSoapInit(SoapObject soapObject);
}
