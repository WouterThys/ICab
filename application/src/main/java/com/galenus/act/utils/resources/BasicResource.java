package com.galenus.act.utils.resources;

import java.io.InputStream;
import java.util.Properties;

public abstract class BasicResource {

    private Properties properties;

    public BasicResource(String propertiesUrl, String fileName) {
        properties = new Properties();
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
}
