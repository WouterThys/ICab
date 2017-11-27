package com.galenus.act.classes.interfaces;

import java.awt.*;

public interface IEditedListener {
    void onValueChanged(Component component, String fieldName, Object previousValue, Object newValue);
    Object getGuiObject();
}
