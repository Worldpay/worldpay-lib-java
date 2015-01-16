package com.worldpay.sdk;

import com.worldpay.api.client.error.dto.ApiError;
import com.worldpay.api.client.error.exception.WorldpayException;
import com.worldpay.sdk.util.HttpUrlConnection;
import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.WorldpayLibraryConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Class to handle HTTP requests and responses.
 */
class Http {

    /**
     * Property for logger component.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Http.class);

    private static final String systemProperties;

    static {
        StringBuilder builder = new StringBuilder();
        builder.append(WorldpayLibraryConstants.OS_NAME_PROP + WorldpayLibraryConstants.COMMA);
        builder.append(WorldpayLibraryConstants.OS_VERSION_PROP + WorldpayLibraryConstants.COMMA);
        builder.append(WorldpayLibraryConstants.OS_ARCH_PROP + WorldpayLibraryConstants.COMMA);
        builder.append(WorldpayLibraryConstants.LANG_VERSION_PROP + WorldpayLibraryConstants.COMMA);
        builder.append(WorldpayLibraryConstants.LIB_VERSION_PROP + WorldpayLibraryConstants.COMMA);
        builder.append(WorldpayLibraryConstants.API_VERSION_PROP + WorldpayLibraryConstants.COMMA);
        builder.append(WorldpayLibraryConstants.OWNER_PROP + WorldpayLibraryConstants.COMMA);
        builder.append(WorldpayLibraryConstants.JAVA_VENDOR_PROP + WorldpayLibraryConstants.COMMA);
        builder.append(WorldpayLibraryConstants.JVM_VENDOR_PROP);
        builder.append(WorldpayLibraryConstants.EOF);
        systemProperties = builder.toString();
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
     *
     * @return the converted object
     */
    public void put(String resourcePath, Object request){
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
            httpURLConnection.setRequestProperty(WorldpayLibraryConstants.AUTHORIZATION, serviceKey);
            httpURLConnection.setRequestProperty(WorldpayLibraryConstants.WP_CLIENT_USER_AGENT, systemProperties);

            DataOutputStream dataOutputStream = null;
            switch (method) {
                case GET:
                    httpURLConnection.setRequestMethod(WorldpayLibraryConstants.GET);
                    break;
                case POST:
                    httpURLConnection.setRequestMethod(WorldpayLibraryConstants.POST);
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    if (request != null) {
                        dataOutputStream.writeBytes(toJson(request));
                    }
                    break;
                case PUT:
                    httpURLConnection.setRequestMethod(WorldpayLibraryConstants.PUT);
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    if (request != null) {
                        dataOutputStream.writeBytes(toJson(request));
                    }
                    break;
                case DELETE:
                    httpURLConnection.setRequestMethod(WorldpayLibraryConstants.DELETE);
                    break;
            }
        } catch (IOException e) {
            LOGGER.error("Problem with the connection", e);
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
            LOGGER.error("Problem with the response", e);
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
        if (connection.getResponseCode() >= WorldpayLibraryConstants.HTTP_ERROR_CODE_300) {
            InputStream is = connection.getErrorStream();
            ApiError error = JsonParser.toObject(is, ApiError.class);
            throw new WorldpayException(error, "API error: " + error.getMessage());
        }
    }
}