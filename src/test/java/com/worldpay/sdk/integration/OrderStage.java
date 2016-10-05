package com.worldpay.sdk.integration;

import com.tngtech.jgiven.CurrentStep;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import com.worldpay.gateway.clearwater.client.core.dto.request.CaptureOrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderAuthorizationRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.gateway.clearwater.client.ui.dto.order.Transaction;
import com.worldpay.sdk.OrderService;
import com.worldpay.sdk.WorldpayRestClient;
import com.worldpay.sdk.util.PropertyUtils;

import static com.tngtech.jgiven.attachment.Attachment.fromText;
import static com.tngtech.jgiven.attachment.MediaType.PLAIN_TEXT_UTF_8;

public class OrderStage extends Stage<OrderStage> {

    @ScenarioState
    OrderService orderService;

    @ProvidedScenarioState
    private OrderResponse orderResponse;

    @ProvidedScenarioState
    private String orderCode;

    @ProvidedScenarioState
    private WorldpayException worldpayException;

    @ProvidedScenarioState
    private Transaction authorizedResponse;

    @ExpectedScenarioState
    CurrentStep currentStep;

    @BeforeStage
    public void init() {
        if (orderService == null) {
            orderService = new WorldpayRestClient(PropertyUtils.serviceKey()).getOrderService();
        }
    }

    public OrderStage wePostAnOrderRequest(OrderRequest orderRequest) {
        try {
            orderResponse = orderService.create(orderRequest);
            currentStep.addAttachment(fromText(orderResponse.toString(), PLAIN_TEXT_UTF_8).withTitle("Response"));
        } catch (WorldpayException e) {
            worldpayException = e;
        }
        return self();
    }

    public OrderStage weAuthorizeTheOrder(OrderAuthorizationRequest orderAuthorizationRequest) {
        orderResponse = orderService.authorize3Ds(orderCode, orderAuthorizationRequest);
        currentStep.addAttachment(fromText(orderResponse.toString(), PLAIN_TEXT_UTF_8).withTitle("Response"));
        return self();
    }

    public OrderStage weRefundTheOrder() {
        orderService.refund(orderCode);
        return self();
    }

    @As("we refund $ from the order")
    public OrderStage weRefundTheOrder(int amount) {
        orderService.refund(orderCode, amount);
        return self();
    }

    public OrderStage weCancelTheOrder() {
        orderService.cancel(orderCode);
        return self();
    }

    public OrderStage weFindTheOrder() {
        authorizedResponse = orderService.findOrder(orderCode);
        currentStep.addAttachment(fromText(authorizedResponse.toString(), PLAIN_TEXT_UTF_8).withTitle("Response"));
        return self();
    }

    @As("we capture $ from the order")
    public OrderStage wePartialCaptureTheOrder(int amount) {
        CaptureOrderRequest captureOrderRequest = new CaptureOrderRequest();
        captureOrderRequest.setCaptureAmount(amount);
        try {
            orderResponse = orderService.capture(captureOrderRequest, orderCode);
            currentStep.addAttachment(fromText(orderResponse.toString(), PLAIN_TEXT_UTF_8).withTitle("Response"));
        } catch (WorldpayException e) {
            worldpayException = e;
        }
        return self();
    }
}
