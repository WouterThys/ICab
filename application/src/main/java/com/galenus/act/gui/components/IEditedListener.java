package com.galenus.act.gui.components;

import java.awt.*;

public interface IEditedListener {
    void onValueChanged(Component component, String fieldName, Object previousValue, Object newValue);
    Object getGuiObject();
}
