package com.worldpay.sdk.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Helper class for providing property values.
 */
public class PropertyUtils {

    /**
     * Default properties file name.
     */
    public static final String PROPERTY_FILE = "worldpay.properties";

    /**
     * Base url property name for connecting to Worlpay API.
     */
    private static final String BASE_URL = "baseUrl";

    /**
     * Service key property name.
     */
    private static final String SERVICE_KEY = "serviceKey";

    /**
     * Properties.
     */
    private static final Properties PROPERTIES = new Properties();

    static {
        InputStream is = PropertyUtils.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
        try {
            PROPERTIES.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load " + PROPERTY_FILE, e);
        }
    }

    /**
     * Value of the the base URL.
     *
     * @return
     */
    public static String baseUrl() {
        return getValue(BASE_URL);
    }

    /**
     * Value of the service key.
     *
     * @return
     */
    public static String serviceKey() {
        return getValue(SERVICE_KEY);
    }

    /**
     * Get value for the specified key.
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return getValue(key);
    }

    private static String getValue(String key) {
        return PROPERTIES.getProperty(key);
    }

}
