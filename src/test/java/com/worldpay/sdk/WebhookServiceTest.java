package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.common.Environment;
import com.worldpay.gateway.clearwater.client.ui.dto.common.BasicNotificationPayload;
import com.worldpay.gateway.clearwater.client.ui.dto.common.NotificationEventType;
import com.worldpay.gateway.clearwater.client.ui.dto.order.OrderStatusChangeNotificationPayload;
import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by MDS on 26/09/2014.
 */
public class WebhookServiceTest {

    private WebhookService webhookService;

    private final String merchantId = "Merchant";
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
        OrderStatusChangeNotificationPayload finalPayload = (OrderStatusChangeNotificationPayload)webhookService.process(requestBody);

        assertThat(finalPayload, is(notNullValue()));
        assertThat(finalPayload.getPaymentStatus(), is(paymentStatus));
        assertThat(finalPayload.getOrderCode(), is(orderCode));
        assertThat(finalPayload.getEnvironment(), is(environment));

        assertThat(finalPayload.getPayload(), is(notNullValue()));
        assertThat(finalPayload.getPayload().getAdminCode(), is(adminCode));
        assertThat(finalPayload.getPayload().getMerchantId(), is(merchantId));
        assertThat(finalPayload.getPayload().getNotificationEventType(), is(notificationEventType));
    }

    private String getNotificationString() {
        BasicNotificationPayload payload = new BasicNotificationPayload(NotificationEventType.ORDER_STATE_CHANGE, adminCode);
        payload.setMerchantId(merchantId);
        OrderStatusChangeNotificationPayload statusPayload = new OrderStatusChangeNotificationPayload();

        statusPayload.setEnvironment(environment);
        statusPayload.setOrderCode(orderCode);
        statusPayload.setPaymentStatus(paymentStatus);
        statusPayload.setPayload(payload);

        return JsonParser.toJson(statusPayload);
    }
}
