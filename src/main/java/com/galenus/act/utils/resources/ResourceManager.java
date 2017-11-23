package com.galenus.act.utils.resources;

import java.io.InputStream;
import java.util.Properties;

public abstract class ResourceManager {

    private Properties properties;

    public ResourceManager(String propertiesUrl, String fileName) {
        properties = new Properties();
        try {
            String resourceFileName = propertiesUrl + fileName;
            InputStream input = getClass().getClassLoader().getResourceAsStream(resourceFileName);
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readString(String key) {
        return properties.getProperty(key);
    }
}
