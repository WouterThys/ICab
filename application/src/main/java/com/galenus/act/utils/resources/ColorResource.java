package com.galenus.act.utils.resources;

import java.awt.*;

public class ColorResource extends BasicResource {

    public ColorResource(String propertiesUrl, String fileName) {
        super(propertiesUrl, fileName);
    }

    public Color readColor(String key) {
        Color color;
        try {
            String cTxt = readProperty(key);
            color = Color.decode(cTxt);
        } catch (Exception e) {
            color = Color.DARK_GRAY;
        }
        return color;
    }
}