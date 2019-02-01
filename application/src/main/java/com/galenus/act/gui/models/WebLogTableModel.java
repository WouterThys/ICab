package com.galenus.act.gui.models;

import com.galenus.act.utils.DateUtils;
import com.galenus.act.classes.managers.web.AsyncWebCall;

import javax.swing.*;

import static com.galenus.act.Application.imageResource;

public class WebLogTableModel extends IAbstractTableModel<AsyncWebCall> {

    private static final String[] COLUMN_NAMES = {"Date", "Method", "ClosedWhileLocked"};
    private static final Class[] COLUMN_CLASSES = {String.class, String.class, ImageIcon.class};

    private static final ImageIcon okImage = imageResource.readImage("Web.Log.Ok");
    private static final ImageIcon nokImage = imageResource.readImage("Web.Log.Nok");

    public WebLogTableModel() {
        super(COLUMN_NAMES, COLUMN_CLASSES);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AsyncWebCall webCall = getItemAt(rowIndex);

        if (webCall != null) {
            switch (columnIndex) {
                case -1:
                    return webCall;
                case 0: // Date
                    return DateUtils.formatTime(webCall.getDate());
                case 1: // Method
                    return webCall.getMethodName();
                case 2: // Success
                    if (webCall.isSuccess()) {
                        return okImage;
                    } else {
                        return nokImage;
                    }
            }
        }
        return null;
    }

}