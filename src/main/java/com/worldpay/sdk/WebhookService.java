package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.common.NotificationPayload;
import com.worldpay.gateway.clearwater.client.ui.dto.order.OrderStatusChangeNotificationPayload;
import org.apache.http.HttpEntity;
import com.worldpay.gateway.clearwater.client.ui.dto.response.WebhookResponse;

/**
 * Created by MDS on 25/09/2014.
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
    public NotificationPayload process(String requestBody) {
        return http.handleRequest(requestBody, OrderStatusChangeNotificationPayload.class);
    }
}
