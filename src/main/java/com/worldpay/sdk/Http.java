package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.response.ApiError;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.sdk.util.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;

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
        DELETE, GET, POST, PUT
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
     * @param baseUri Base URI for connection
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
     * @param request the Object which needs to be serialized and sent as POST payload, may be null
     * @param responseType the type of the return value
     * @return the converted object
     */
    public <T> T post(String resourcePath, Object request, final Class<T> responseType) {
        Request postRequest = createRequest(RequestMethod.POST, resourcePath);

        if (request != null) {
            String jsonString = toJson(request);
            postRequest.bodyString(jsonString, ContentType.APPLICATION_JSON);
        }

        return execute(postRequest, responseType);
    }

    /**
     * Create a new resource using POST
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param request the Object which needs to be serialized and sent as POST payload, may be null
     */
     public void post(String resourcePath, Object request) {
        Request postRequest = createRequest(RequestMethod.POST, resourcePath);

        if (request != null) {
            String jsonString = toJson(request);
            postRequest.bodyString(jsonString, ContentType.APPLICATION_JSON);
        }

        execute(postRequest);
    }

    /**
     * Updates an existing resource using PUT
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param request the Object which needs to be serialised and sent as payload, may be null
     */
    public void put(String resourcePath, Object request) {
        Request putRequest = createRequest(RequestMethod.PUT, resourcePath);

        if (request != null) {
            String jsonString = toJson(request);
            putRequest.bodyString(jsonString, ContentType.APPLICATION_JSON);
        }
        execute(putRequest);
    }

    /**
     * Updates an existing resource using PUT and return the parsed response.
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param request the Object which needs to be serialised and sent as payload, may be null
     * @param responseType the type of the return value
     * @return the converted object
     */
    public <T> T put(String resourcePath, Object request, final Class<T> responseType) {
        Request putRequest = createRequest(RequestMethod.PUT, resourcePath);

        if (request != null) {
            String jsonString = toJson(request);
            putRequest.bodyString(jsonString, ContentType.APPLICATION_JSON);
        }

        return execute(putRequest, responseType);
    }

    /**
     * Return the representation obtained by GET.
     *
     * @param resourcePath the location of the resource e.g. /order/123
     * @param responseType the type of the return value
     * @return the converted object
     */
    public <T> T get(String resourcePath, final Class<T> responseType) {
        Request getRequest = createRequest(RequestMethod.GET, resourcePath);
        return execute(getRequest, responseType);
    }

    /**
     * Parse an incoming request and deserialize the body
     *
     * @param requestBody the incoming Http request body
     * @param dataType the type to which to deserialise the body content
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
    private <T> T execute(Request request, final Class<T> responseType) {
        try {
            return request.execute().handleResponse(getHandler(responseType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the request to be used when no response is expected
     *
     * @param request the request
     */
    private void execute(Request request) {
        try {
            HttpResponse response = request.execute().returnResponse();
            HttpEntity entity = response.getEntity();
            errorHandler(response, entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create an appropriate request.
     *
     * @param method the request method
     * @param uri the resource uri
     * @return new request object
     */
    private Request createRequest(RequestMethod method, String uri) {
        Request request = null;
        String fullUri = baseUri + uri;

        switch (method) {
            case GET:
                request = Request.Get(fullUri);
                break;
            case POST:
                request = Request.Post(fullUri);
                break;
            case PUT:
                request = Request.Put(fullUri);
                break;
            case DELETE:
                request = Request.Delete(fullUri);
                break;
            }

        return request.addHeader(HttpHeaders.ACCEPT, APPLICATION_JSON)
                        .addHeader(HttpHeaders.AUTHORIZATION, serviceKey)
                        .connectTimeout(CONNECTION_TIMEOUT)
                        .socketTimeout(SOCKET_TIMEOUT);
    }

    /**
     * Return the handler for the response type.
     *
     * @param responseType the expected response type
     *
     * @return an instance of {@code responseType}
     */
    private <T> ResponseHandler<T> getHandler(final Class<T> responseType) {
        return new ResponseHandler<T>() {

            public T handleResponse(HttpResponse response) throws IOException {
                HttpEntity entity = response.getEntity();
                errorHandler(response, entity);
                return JsonParser.toObject(entity.getContent(), responseType);
            }
        };
    }

    /**
     * Examines the {@code response} and throws {@link WorldpayException} if an error response is detected
     *
     * @param response {@link HttpResponse} to be examin
     * @param entity   {@link HttpEntity}
     *
     * @throws IOException       if it fails to parse the error message contained in the response
     * @throws WorldpayException if an erroneous response is detected
     */
    private void errorHandler(HttpResponse response, HttpEntity entity) throws IOException {
        StatusLine statusLine = response.getStatusLine();

        if (statusLine.getStatusCode() >= 300) {
            ApiError error = JsonParser.toObject(entity.getContent(), ApiError.class);
            throw new WorldpayException(error, "API error: " + error.getMessage());
        }
    }
}
