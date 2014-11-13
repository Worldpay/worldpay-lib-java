package com.worldpay.sdk;

import com.worldpay.api.client.error.dto.ApiError;
import com.worldpay.gateway.clearwater.service.core.exception.WpgException;

import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.PropertyUtils;
import com.worldpay.sdk.util.WorldPayHttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * Class to handle HTTP requests and responses.
 */
class Http {

    /**
     * Property for logger component.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Http.class);

    /**
     * Connection timeout in milliseconds.
     */
    private static final int CONNECTION_TIMEOUT = 10000;

    /**
     * Socket timeout in milliseconds.
     */
    private static final int SOCKET_TIMEOUT = 10000;

    /**
     * JSON header value.
     */
    private static final String APPLICATION_JSON = "application/json";

    public static final String COMMA = ",";

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
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        try {
            url = new URL(fullUri);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            httpURLConnection.setReadTimeout(SOCKET_TIMEOUT);
            httpURLConnection.setRequestProperty(WorldPayHttpHeaders.ACCEPT, APPLICATION_JSON);
            httpURLConnection.setRequestProperty(WorldPayHttpHeaders.AUTHORIZATION, serviceKey);
            httpURLConnection.setRequestProperty(WorldPayHttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            httpURLConnection
                .setRequestProperty(WorldPayHttpHeaders.WP_CLIENT_USER_AGENT, getWorldPayClientUserAgentDetails());
            DataOutputStream dataOutputStream = null;
            switch (method) {
                case GET:
                    httpURLConnection.setRequestMethod("GET");
                    break;
                case POST:
                    httpURLConnection.setRequestMethod("POST");
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    if (request != null) {
                        dataOutputStream.writeBytes(toJson(request));
                    }
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    break;
                case PUT:
                    httpURLConnection.setRequestMethod("PUT");
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    if (request != null) {
                        dataOutputStream.writeBytes(toJson(request));
                    }
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    break;
                case DELETE:
                    httpURLConnection.setRequestMethod("DELETE");
                    break;
            }
        } catch (IOException e) {
            LOGGER.error("Problem with the connection", e);
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
            LOGGER.error("Problem with the response", e);
        }
        return null;
    }

    /**
     * Examines the {@code response} and throws {@link WpgException} if an error response is detected
     *
     * @throws IOException       if it fails to parse the error message contained in the response
     * @throws WpgException if an erroneous response is detected
     */
    private void errorHandler(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() >= 300) {
            InputStream is = connection.getErrorStream();
            ApiError error = JsonParser.toObject(is, ApiError.class);
            throw new WpgException("API error: " + error.getMessage(), error.getHttpStatusCode());
        }
    }

    /**
     * Properties required for Worldpay. Reads from system properites and worldpay.properties.
     *
     * @return string of properies with key=value,key=value,...
     */
    private String getWorldPayClientUserAgentDetails() {
        Properties systemProperties = System.getProperties();
        StringBuffer buffer = new StringBuffer();
        buffer.append("os.name=" + systemProperties.getProperty("os.name") + COMMA);
        buffer.append("os.version=" + systemProperties.getProperty("os.version") + COMMA);
        buffer.append("os.arch=" + systemProperties.getProperty("os.arch") + COMMA);
        buffer.append("lang.version=" + systemProperties.getProperty("java.vm.specification.version") + COMMA);
        buffer.append("lib.version=" + PropertyUtils.getProperty("lib.version") + COMMA);
        buffer.append("api.version=" + PropertyUtils.getProperty("api.version") + COMMA);
        buffer.append("lang=" + PropertyUtils.getProperty("language") + COMMA);
        buffer.append("owner=" + PropertyUtils.getProperty("owner") + COMMA);
        buffer.append("java.vendor=" + systemProperties.getProperty("java.vendor") + COMMA);
        buffer.append("jvm.vendor=" + systemProperties.getProperty("java.vm.vendor"));
        return buffer.toString();
    }
}