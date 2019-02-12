package com.galenus.act.utils.resources;

import java.io.InputStream;
import java.util.Properties;

public abstract class BasicResource {

    private Properties properties;

    BasicResource() {
        properties = new Properties();
    }

    public BasicResource(String propertiesUrl, String fileName) {
        this();
        initialize(propertiesUrl, fileName);
    }

    public void initialize(String propertiesUrl, String fileName) {
        try {
            String resourceFileName = propertiesUrl + fileName;
            InputStream input = getClass().getClassLoader().getResourceAsStream(resourceFileName);
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readProperty(String key) {
        String property = properties.getProperty(key);
        if (property != null) {
            property = property.trim();
        } else {
            property = "";
        }
        return property;
    }

    public void writeString(String key, String value) {
        properties.setProperty(key, value);
    }

    public void writeInt(String key, int value) {
        writeString(key, String.valueOf(value));
    }

    public void writeBoolean(String key, boolean value) {
        writeString(key, String.valueOf(value));
    }
}
