package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.common.Environment;
import com.worldpay.gateway.clearwater.client.ui.dto.common.NotificationEventType;

import java.util.Objects;

/**
 * Body content of the webhook notification messages.
 */
public class Notification {

    /**
     * The identifier of this notification request. Each time a delivery is attempted, this id will be updated.
     */
    private String id;

    /**
     * The webhook that triggered this notification.
     */
    private String webhookId;

    /**
     * The code of the {@code Order} whose status was updated.
     */
    private String orderCode;

    /**
     * The new {@code OrderStatus}.
     */
    private String paymentStatus;

    /**
     * Which environment the order comes from.
     */
    private Environment environment;

    /**
     * The merchant id
     */
    private String merchantId;

    /**
     * The aggregate merchant id
     */
    private String aggregateMerchantId;

    /**
     * The type of notification event
     */
    private NotificationEventType notificationEventType;

    /**
     * The admin code
     */
    private String adminCode;

    /**
     * The merchant code
     */
    private String merchantCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAggregateMerchantId() {
        return aggregateMerchantId;
    }

    public void setAggregateMerchantId(String aggregateMerchantId) {
        this.aggregateMerchantId = aggregateMerchantId;
    }

    public NotificationEventType getNotificationEventType() {
        return notificationEventType;
    }

    public void setNotificationEventType(NotificationEventType notificationEventType) {
        this.notificationEventType = notificationEventType;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, webhookId, orderCode, paymentStatus, environment);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Notification other = (Notification) obj;
        return Objects.equals(this.id, other.id) && Objects.equals(this.webhookId, other.webhookId) && Objects
                .equals(this.orderCode, other.orderCode) && Objects.equals(this.paymentStatus, other.paymentStatus)
                && Objects.equals(this.environment, other.environment);
    }
}
