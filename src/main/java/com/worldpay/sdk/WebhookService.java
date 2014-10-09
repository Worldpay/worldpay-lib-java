package com.worldpay.sdk;

/**
 * Service for handlig incoming webhook notifications
 */
public class WebhookService extends AbstractService {

    WebhookService(Http http) {
        super(http);
    }

    /**
     * Parse an incoming request and return notification payload
     *
     * @param requestBody the body of the incoming http request
     *
     * @return the deserialised notification payload
     */
    public Notification process(String requestBody) {
        return http.handleRequest(requestBody, Notification.class);
    }
}
