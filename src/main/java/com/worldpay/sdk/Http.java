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

package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.ApiError;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.sdk.util.HttpUrlConnection;
import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.WorldpayLibraryConstants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static com.worldpay.sdk.util.WorldpayLibraryConstants.*;

/**
 * Class to handle HTTP requests and responses.
 */
class Http {

    private static final String systemProperties;

    static {
        systemProperties = OS_NAME_PROP + COMMA +
                           OS_VERSION_PROP + COMMA +
                           OS_ARCH_PROP + COMMA +
                           LANG_VERSION_PROP + COMMA +
                           API_VERSION_PROP + COMMA +
                           LANG_PROP + COMMA +
                           OWNER_PROP + COMMA +
                           JAVA_VENDOR_PROP + COMMA +
                           JVM_VENDOR_PROP + COMMA +
                           BUILD;
    }

    /**
     * Enumeration for HTTP methods.
     */
    private enum RequestMethod {
        DELETE,
        GET,
        POST,
        PUT
    }

    /**
     * Base URI.
     */
    private String baseUri;

    /**
     * Service key.
     */
    private String serviceKey;

    /**
     * Http object.
     *
     * @param baseUri    Base URI for connection
     * @param serviceKey default service key for connection
     */
    public Http(String baseUri, String serviceKey) {
        this.baseUri = baseUri;
        this.serviceKey = serviceKey;
    }

    /**
     * Create a new resource using POST and return the parsed response.
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param request      the Object which needs to be serialized and sent as POST payload, may be null
     * @param responseType the type of the return value
     *
     * @return the converted object
     */
    public <T> T post(String resourcePath, Object request, final Class<T> responseType) {
        HttpURLConnection postRequest = createRequest(RequestMethod.POST, resourcePath, request);
        return execute(postRequest, responseType);
    }

    /**
     * Create a new resource using POST
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param request      the Object which needs to be serialized and sent as POST payload, may be null
     */
    public void post(String resourcePath, Object request) {
        HttpURLConnection postRequest = createRequest(RequestMethod.POST, resourcePath, request);
        execute(postRequest);
    }

    /**
     * Updates an existing resource using PUT and return the parsed response.
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param request      the Object which needs to be serialised and sent as payload, may be null
     * @param responseType the type of the return value
     *
     * @return the converted object
     */
    public <T> T put(String resourcePath, Object request, final Class<T> responseType) {
        HttpURLConnection putRequest = createRequest(RequestMethod.PUT, resourcePath, request);
        return execute(putRequest, responseType);
    }

    /**
     * Updates an existing resource using PUT with no return.
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param request      the Object which needs to be serialised and sent as payload, may be null
     */
    public void put(String resourcePath, Object request) {
        HttpURLConnection putRequest = createRequest(RequestMethod.PUT, resourcePath, request);
        execute(putRequest);
    }

    /**
     * Return the representation obtained by GET.
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param responseType the type of the return value
     *
     * @return the converted object
     */
    public <T> T get(String resourcePath, final Class<T> responseType) {
        HttpURLConnection getRequest = createRequest(RequestMethod.GET, resourcePath, responseType);
        return execute(getRequest, responseType);
    }

    /**
     * Delete an existing resource using DELETE with no return.
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param request      the Object which needs to be serialised and sent as payload, may be null
     */
    public void delete(String resourcePath, Object request) {
        HttpURLConnection putRequest = createRequest(RequestMethod.DELETE, resourcePath, request);
        execute(putRequest);
    }

    /**
     * Convert object to string representation.
     *
     * @param request object to convert
     *
     * @return String representation of the {@code request}
     */
    private String toJson(Object request) {
        try {
            return JsonParser.toJson(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the request.
     *
     * @param request      the request
     * @param responseType the response type
     *
     * @return an instance of the {@code responseType}
     */
    private <T> T execute(HttpURLConnection request, final Class<T> responseType) {
        return getHandler(request, responseType);
    }

    /**
     * Execute the request to be used when no response is expected
     *
     * @param request the request
     */
    private void execute(HttpURLConnection request) {
        try {
            errorHandler(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create an appropriate request.
     *
     * @param method the request method
     * @param uri    the resource uri
     *
     * @return new request object
     */
    private HttpURLConnection createRequest(RequestMethod method, String uri, Object request) {
        String fullUri = baseUri + uri;
        HttpURLConnection httpURLConnection = HttpUrlConnection.getConnection(fullUri);
        try {
            httpURLConnection.setRequestProperty(AUTHORIZATION, serviceKey);
            final String propertiesWithVersion = systemProperties.concat(getVersion());
            httpURLConnection.setRequestProperty(WP_CLIENT_USER_AGENT, propertiesWithVersion);

            DataOutputStream dataOutputStream;
            switch (method) {
                case GET:
                    httpURLConnection.setRequestMethod(GET);
                    break;
                case POST:
                    httpURLConnection.setRequestMethod(POST);
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    if (request != null) {
                        dataOutputStream.writeBytes(toJson(request));
                    }
                    break;
                case PUT:
                    httpURLConnection.setRequestMethod(PUT);
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    if (request != null) {
                        dataOutputStream.writeBytes(toJson(request));
                    }
                    break;
                case DELETE:
                    httpURLConnection.setRequestMethod(DELETE);
                    break;
            }
        } catch (IOException e) {
            throw new WorldpayException(e.getMessage());
        }
        return httpURLConnection;
    }

    /**
     * @param connection   http url connection
     * @param responseType the expected response type
     *
     * @return an instance of {@code responseType}
     */
    private <T> T getHandler(HttpURLConnection connection, final Class<T> responseType) {
        try {
            errorHandler(connection);
            InputStream is = connection.getInputStream();
            return JsonParser.toObject(is, responseType);
        } catch (IOException e) {
            throw new WorldpayException(e.getMessage());
        }
    }

    /**
     * Examines the {@code response} and throws {@link WorldpayException} if an error response is detected
     *
     * @throws IOException       if it fails to parse the error message contained in the response
     * @throws WorldpayException if an erroneous response is detected
     */
    private void errorHandler(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() >= HTTP_ERROR_CODE_300) {
            InputStream is = connection.getErrorStream();
            ApiError error = JsonParser.toObject(is, ApiError.class);
            throw new WorldpayException(error, "API error: " + error.getMessage());
        }
    }

    /**
     * Looks up the manifest file and pulls the version out
     *
     * @return Version
     */
    public String getVersion() {
        Class clazz = this.getClass();
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        final String versionNotAvailable = "N/A";
        if (!classPath.startsWith("jar")) {
            // Class not from JAR
            return versionNotAvailable;
        }
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
        try {
            Manifest manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attr = manifest.getMainAttributes();
            final String attrValue = attr.getValue("Implementation-Version");
            if (attrValue != null && attrValue.trim().length() > 0) {
                return attrValue;
            } else {
                return versionNotAvailable;
            }
        } catch (IOException e) {
            return versionNotAvailable;
        }
    }
}
