package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.common.Environment;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.gateway.clearwater.client.ui.dto.common.AbstractNotificationPayload;
import com.worldpay.gateway.clearwater.client.ui.dto.common.NotificationEventType;
import com.worldpay.gateway.clearwater.client.ui.dto.order.OrderStatusChangeNotificationPayload;
import com.worldpay.gateway.clearwater.client.ui.dto.request.WebhookRequest;
import com.worldpay.gateway.clearwater.client.ui.dto.response.WebhookListResponse;
import com.worldpay.gateway.clearwater.client.ui.dto.response.WebhookResponse;
import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.PropertyUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Test that the Webhook notifications are correctly handled by the Webhook service
 */
public class WebhookServiceTest {

    public static final String WEB_HOOK_URL = "https://logger.worldpaydemo.com/logger";

    private final String merchantId = "Merchant";

    private final String aggregateMerchantId = "AggregateMerchant";

    private final String merchantCode = "MerchantCode";

    private final String adminCode = "adminCode";

    private final String orderCode = "orderCode";

    private final String paymentStatus = "paid";

    private final Environment environment = Environment.TEST;

    private final NotificationEventType notificationEventType = NotificationEventType.ORDER_STATE_CHANGE;

    private WebhookService webhookService;

    @Before
    public void setup() {
        webhookService = new WorldpayRestClient(PropertyUtils.serviceKey()).getWebhookService();
    }

    @Test

    public void shouldHandleNotification() {
        String requestBody = getNotificationString();

        AbstractNotificationPayload finalPayload = webhookService.process(requestBody);
        assertThat("Notification Type", finalPayload, instanceOf(OrderStatusChangeNotificationPayload.class));

        OrderStatusChangeNotificationPayload orderNotification = (OrderStatusChangeNotificationPayload) finalPayload;

        assertThat("Order notification", orderNotification, is(notNullValue()));
        assertThat("Payment status", orderNotification.getPaymentStatus(), is(paymentStatus));
        assertThat("Order code", orderNotification.getOrderCode(), is(orderCode));
        assertThat("Environment", orderNotification.getEnvironment(), is(environment));

        assertThat("Admin code", orderNotification.getAdminCode(), is(adminCode));
        assertThat("Merchant id", orderNotification.getMerchantId(), is(merchantId));
        assertThat("Notification event type", orderNotification.getNotificationEventType(), is(notificationEventType));
    }

    @Test
    @Ignore
    public void shouldCreateWebhook() {
        WebhookRequest request = new WebhookRequest();
        List<String> events = new ArrayList<String>();
        events.add("ALL");
        request.setEvents(events);
        request.setWebHookUrl(WEB_HOOK_URL);
        WebhookResponse response = webhookService.create(PropertyUtils.getProperty("merchantId"), request);
        assertThat("webhook url", response.getWebHookId(), is(notNullValue()));
        assertThat("webhook events", response.getEvents().size(), is(greaterThan(0)));
    }

    @Test
    @Ignore
    public void shouldNotCreateWebhookWithInvalidEvent() {
        WebhookRequest request = new WebhookRequest();
        List<String> events = new ArrayList<String>();
        events.add("invalid-event");
        request.setEvents(events);
        request.setWebHookUrl(WEB_HOOK_URL);
        try {
            webhookService.create(PropertyUtils.getProperty("merchantId"), request);
        } catch (WorldpayException e) {
            assertThat("Invalid event type", e.getApiError().getCustomCode(), is("BAD_REQUEST"));
            assertThat("Invalid event type", e.getApiError().getMessage(), is("Invalid event type"));
        }
    }

    @Test
    @Ignore
    public void shouldNotCreateWebhookWithInvalidWebhookUrl() {
        WebhookRequest request = new WebhookRequest();
        List<String> events = new ArrayList<String>();
        events.add("ALL");
        request.setEvents(events);
        request.setWebHookUrl("invalid-url");
        try {
            WebhookResponse response = webhookService.create(PropertyUtils.getProperty("merchantId"), request);
        } catch (WorldpayException e) {
            assertThat("Invalid event type", e.getApiError().getCustomCode(), is("BAD_REQUEST"));
            assertThat("Invalid event type", e.getApiError().getMessage(), is("URL is not valid"));
        }
    }

    @Test
    @Ignore
    public void shouldGetWebhooksForValidMerchant() {
        WebhookListResponse response = webhookService.getWebhooks(PropertyUtils.getProperty("merchantId"));
        assertThat("webhooks for the given merchant", response.getWebHooks().size(), is(greaterThan(0)));
    }

    @Test
    @Ignore
    public void shouldNotGetWebhooksForInvalidMerchant() {
        try {
            webhookService.getWebhooks("invalid-merchant");
        } catch (WorldpayException e) {
            assertThat("Access should be denied", e.getApiError().getCustomCode(), is("ACCESS_DENIED"));
            assertThat("Access should be denied", e.getApiError().getMessage(), is("Access is denied"));
        }

    }


    /**
     * Utility to build the string representation of the data to expect via the webhook notification
     */
    private String getNotificationString() {
        OrderStatusChangeNotificationPayload statusPayload =
            new OrderStatusChangeNotificationPayload(merchantId, aggregateMerchantId, adminCode, merchantCode,
                                                     orderCode, paymentStatus, environment);

        return JsonParser.toJson(statusPayload);
    }
}
