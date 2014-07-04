package com.worldpay.sdk.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

/**
 * Class used for converting to and from JSON.
 */
public final class JsonParser {

    /**
     * Parser.
     */
    private static final ObjectMapper PARSER = new ObjectMapper();

    /**
     * Private constructor.
     */
    private JsonParser() {
    }

    /**
     * Convert an object to JSON representation.
     *
     * @param object to convert
     *
     * @return the string representation
     */
    public static String toJson(Object object) {
        try {
            return PARSER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a string to Object representation.
     *
     * @param json the string to convert
     * @param type the type of object
     *
     * @return the object created from the string
     */
    public static <T> T toObject(String json, Class<T> type) {
        try {
            return PARSER.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert an input stream to Object representation.
     *
     * @param in   {@link InputStream}
     * @param type {@link Class} type
     *
     * @return Data read into an object of type
     */
    public static <T> T toObject(InputStream in, Class<T> type) {
        try {
            return PARSER.readValue(in, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
