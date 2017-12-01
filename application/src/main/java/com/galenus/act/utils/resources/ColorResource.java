package com.galenus.act.utils.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ColorResource extends ResourceManager {

    public ColorResource(String propertiesUrl, String fileName) {
        super(propertiesUrl, fileName);
    }

    public Color readColor(String key) {
        Color color;
        try {
            String cTxt = readString(key);
            color = Color.decode(cTxt);
        } catch (Exception e) {
            color = Color.DARK_GRAY;
        }
        return color;
    }
}