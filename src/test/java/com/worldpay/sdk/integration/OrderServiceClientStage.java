package com.worldpay.sdk.integration;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.worldpay.gateway.clearwater.client.core.dto.request.CaptureOrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.sdk.OrderService;
import com.worldpay.sdk.WorldpayRestClient;
import com.worldpay.sdk.util.PropertyUtils;

public class OrderServiceClientStage extends Stage<OrderServiceClientStage> {

    @ProvidedScenarioState
    private OrderService orderService;

    @ProvidedScenarioState
    private String orderCode;

    @BeforeStage
    private void init() {
        orderService = new WorldpayRestClient(PropertyUtils.serviceKey()).getOrderService();
    }

    public OrderServiceClientStage aWorldpayRestClientWithServiceKey(String serviceKey) {
        orderService = new WorldpayRestClient(serviceKey).getOrderService();
        return self();
    }

    public OrderServiceClientStage aExcisingOrder(OrderRequest orderRequest) {
        OrderResponse response = orderService.create(orderRequest);
        orderCode = response.getOrderCode();
        return self();
    }

    public OrderServiceClientStage wePartialCaptureTheOrder(int amount) {
        CaptureOrderRequest captureOrderRequest = new CaptureOrderRequest();
        captureOrderRequest.setCaptureAmount(amount);
        orderService.capture(captureOrderRequest, orderCode);
        return self();
    }

    public OrderServiceClientStage weCaptureTheOrder() {
        CaptureOrderRequest captureOrderRequest = new CaptureOrderRequest();
        orderService.capture(captureOrderRequest, orderCode);
        return self();
    }

    public OrderServiceClientStage thatIsAuthorizeOnly() {
        return self();
    }
}
