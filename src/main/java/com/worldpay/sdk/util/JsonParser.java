/*
 * Copyright 2013 Worldpay
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.worldpay.sdk.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
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

    static {
        PARSER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

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
     * @param <T>  the class of the value
     * @param json the {@link String} to convert
     * @param type the {@link Class} of the value
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
     * @param <T>  the class of the value
     * @param in   the {@link InputStream} to get the object to convert from
     * @param type the {@link Class} of the value
     *
     * @return the object created from the InputStream
     */
    public static <T> T toObject(InputStream in, Class<T> type) {
        try {
            return PARSER.readValue(in, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
