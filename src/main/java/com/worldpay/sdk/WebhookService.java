package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.common.AbstractNotificationPayload;
import com.worldpay.gateway.clearwater.client.ui.dto.request.WebhookRequest;
import com.worldpay.gateway.clearwater.client.ui.dto.response.WebhookListResponse;
import com.worldpay.gateway.clearwater.client.ui.dto.response.WebhookResponse;

/**
 * Service for handlig incoming webhook notifications
 */
public class WebhookService extends AbstractService {

    public static final String WEBHOOK_URL = "/merchants/%s/settings/webhooks";
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

    public WebhookResponse create(String merchantId, WebhookRequest request) {
        return http.post(String.format(WEBHOOK_URL, merchantId), request, WebhookResponse.class);
    }

    public WebhookListResponse getWebhooks(String merchantId) {
        return http.get(String.format(WEBHOOK_URL, merchantId), WebhookListResponse.class);
    }
}
