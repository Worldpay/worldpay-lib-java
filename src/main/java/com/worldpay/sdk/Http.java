package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.response.ApiError;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.WorldPayHttpHeaders;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to handle HTTP requests and responses.
 */
class Http {

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
     */
    public void put(String resourcePath, Object request) {
        HttpURLConnection putRequest = createRequest(RequestMethod.PUT, resourcePath, request);
        execute(putRequest);
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
     * Parse an incoming request and deserialise the body
     *
     * @param requestBody the incoming Http request body
     * @param dataType    the type to which to deserialise the body content
     *
     * @return the converted object
     */
    public <T> T handleRequest(String requestBody, final Class<T> dataType) {
        try {
            return JsonParser.toObject(requestBody, dataType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            switch (method) {
                case GET:
                    httpURLConnection.setRequestMethod("GET");
                    break;
                case POST:
                    httpURLConnection.setRequestMethod("POST");
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    if (request != null) {
                        dataOutputStream.writeBytes(toJson(request));
                    }
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    break;
                case PUT:
                    httpURLConnection.setRequestMethod("PUT");
                    break;
                case DELETE:
                    httpURLConnection.setRequestMethod("DELETE");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
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
/*            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            System.out.println(response.toString());*/
            return JsonParser.toObject(is, responseType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Examines the {@code response} and throws {@link WorldpayException} if an error response is detected
     *
     * @throws IOException       if it fails to parse the error message contained in the response
     * @throws WorldpayException if an erroneous response is detected
     */
    private void errorHandler(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() >= 300) {
            InputStream is = connection.getErrorStream();
            ApiError error = JsonParser.toObject(is, ApiError.class);
            throw new WorldpayException(error, "API error: " + error.getMessage());
        }
    }
}