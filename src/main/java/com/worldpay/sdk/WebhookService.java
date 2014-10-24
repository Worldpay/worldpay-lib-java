package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.common.AbstractNotificationPayload;

/**
 * Service for handlig incoming webhook notifications
 */
public class WebhookService extends AbstractService {

    /**
     * Constructor
     *
     * @param http {@link Http}
     */
    protected WebhookService(Http http) {
        super(http);
    }

    /**
     * Parse an incoming request and return notification payload
     *
     * @param requestBody the body of the incoming http request
     *
     * @return the deserialised notification payload
     */
    public AbstractNotificationPayload process(String requestBody) {
        return http.handleRequest(requestBody, AbstractNotificationPayload.class);
    }
}
