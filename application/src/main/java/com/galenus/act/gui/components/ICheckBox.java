package com.galenus.act.gui.components;

import com.galenus.act.classes.interfaces.IEditedListener;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ICheckBox extends JCheckBox implements ItemListener {

    private IEditedListener editedListener;
    private String fieldName;

    public ICheckBox() {
    }

    public ICheckBox(boolean enabled) {
        super();
        setEnabled(enabled);
    }

    public ICheckBox(String text) {
        super(text);
    }

    public ICheckBox(String text, boolean selected) {
        super(text, selected);
    }

    public void addEditedListener(IEditedListener listener, String fieldName) {
        this.addItemListener(this);
        this.editedListener = listener;
        this.setFieldName(fieldName);
    }

    private void setFieldName(String fieldName) {
        String firstChar = String.valueOf(fieldName.charAt(0));
        if (firstChar.equals(firstChar.toLowerCase())) {
            fieldName = firstChar.toUpperCase() + fieldName.substring(1, fieldName.length());
        }

        this.fieldName = fieldName;
    }

    public void itemStateChanged(ItemEvent e) {
        if (this.editedListener != null) {
            try {
                Object guiObject = this.editedListener.getGuiObject();
                if (guiObject != null) {
                    ICheckBox checkBox = (ICheckBox)e.getSource();
                    String newVal = String.valueOf(checkBox.isSelected());
                    Method setMethod = guiObject.getClass().getMethod("set" + this.fieldName, Boolean.TYPE);
                    Method getMethod = guiObject.getClass().getMethod("is" + this.fieldName);
                    String oldVal = String.valueOf(getMethod.invoke(guiObject));
                    setMethod.invoke(guiObject, Boolean.valueOf(newVal));
                    this.editedListener.onValueChanged(this, this.fieldName, oldVal, newVal);
                }
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException var8) {
                var8.printStackTrace();
            }
        }

    }

}
