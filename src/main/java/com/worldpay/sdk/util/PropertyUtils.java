package com.worldpay.sdk.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper class for providing property values. It will return a System property value for the specified name which can
 * be overridden using a property file.
 */
public class PropertyUtils {

    public static final String PROPERTY_FILE = "worldpay.properties";

    private static final String BASE_URL = "baseUrl";

    private static final String SERVICE_KEY = "serviceKey";

    private static final Properties PROPERTIES = new Properties();

    static {
        InputStream is = PropertyUtils.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
        try {
            PROPERTIES.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load " + PROPERTY_FILE, e);
        }
    }

    public static String baseUrl() {
        return getValue(BASE_URL);
    }

    public static String serviceKey() {
        return getValue(SERVICE_KEY);
    }

    public static String getProperty(String key) {
        return getValue(key);
    }

    private static String getValue(String key) {
        return PROPERTIES.getProperty(key);
    }

}
