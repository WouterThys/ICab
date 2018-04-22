package com.galenus.act.utils.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class ImageResource extends BasicResource {

    public ImageResource(String propertiesUrl, String fileName) {
        super(propertiesUrl, fileName);
    }

    public ImageIcon readImage(String key) {
        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream("icons/" + readProperty(key));
            return new ImageIcon(ImageIO.read(is));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
