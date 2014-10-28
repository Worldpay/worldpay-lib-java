package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.common.Environment;
import com.worldpay.gateway.clearwater.client.ui.dto.common.AbstractNotificationPayload;
import com.worldpay.gateway.clearwater.client.ui.dto.common.NotificationEventType;
import com.worldpay.gateway.clearwater.client.ui.dto.order.OrderStatusChangeNotificationPayload;
import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

import javax.management.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Test that the Webhook notifications are correctly handled by the Webhook service
 */
public class WebhookServiceTest {

    private WebhookService webhookService;

    private final String merchantId = "Merchant";
    private final String aggregateMerchantId = "AggregateMerchant";
    private final String merchantCode = "MerchantCode";
    private final String adminCode = "adminCode";
    private final String orderCode = "orderCode";
    private final String paymentStatus = "paid";
    private final Environment environment = Environment.TEST;
    private final NotificationEventType notificationEventType = NotificationEventType.ORDER_STATE_CHANGE;


    @Before
    public void setup() {
        webhookService = new WorldpayRestClient(PropertyUtils.serviceKey()).getWebhookService();
    }

    @Test
    public void shouldHandleNotification() {
        String requestBody = getNotificationString();

        AbstractNotificationPayload finalPayload = webhookService.process(requestBody);
        assertThat("Notification Type", finalPayload, instanceOf(OrderStatusChangeNotificationPayload.class));

        OrderStatusChangeNotificationPayload orderNotification = (OrderStatusChangeNotificationPayload)finalPayload;

        assertThat("Order notification", orderNotification, is(notNullValue()));
        assertThat("Payment status", orderNotification.getPaymentStatus(), is(paymentStatus));
        assertThat("Order code", orderNotification.getOrderCode(), is(orderCode));
        assertThat("Environment", orderNotification.getEnvironment(), is(environment));

        assertThat("Admin code", orderNotification.getAdminCode(), is(adminCode));
        assertThat("Merchant id", orderNotification.getMerchantId(), is(merchantId));
        assertThat("Notification event type", orderNotification.getNotificationEventType(), is(notificationEventType));
    }

    /**
     * Utility to build the string representation of the data to expect via the webhook notification
     */
    private String getNotificationString() {
        OrderStatusChangeNotificationPayload statusPayload =
                new OrderStatusChangeNotificationPayload(
                        merchantId,
                        aggregateMerchantId,
                        adminCode,
                        merchantCode,
                        orderCode,
                        paymentStatus,
                        environment);

        return JsonParser.toJson(statusPayload);
    }
}
