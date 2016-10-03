package com.worldpay.sdk.integration;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.worldpay.gateway.clearwater.client.core.dto.CountryCode;
import com.worldpay.gateway.clearwater.client.core.dto.CurrencyCode;
import com.worldpay.gateway.clearwater.client.core.dto.common.Address;
import com.worldpay.gateway.clearwater.client.core.dto.common.DeliveryAddress;
import com.worldpay.gateway.clearwater.client.core.dto.common.Entry;
import com.worldpay.gateway.clearwater.client.core.dto.request.CaptureOrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.sdk.OrderService;
import com.worldpay.sdk.TokenService;
import com.worldpay.sdk.WorldpayRestClient;
import com.worldpay.sdk.util.PropertyUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderServiceClientStage extends Stage<OrderServiceClientStage> {

    @ProvidedScenarioState
    private OrderService orderService;

    @ProvidedScenarioState
    private String orderCode;

    private TokenService tokenService;

    private TokenResponse tokenResponse;

    @BeforeStage
    private void init() {
        WorldpayRestClient restClient = new WorldpayRestClient(PropertyUtils.serviceKey());
        orderService = restClient.getOrderService();
        tokenService = restClient.getTokenService();
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

    public OrderServiceClientStage weCreateAToken(TokenRequest tokenRequest) {
        tokenResponse = tokenService.create(tokenRequest);
        return self();
    }

    public OrderServiceClientStage weCreateAnOrder() {
        OrderRequest orderRequest = createOrderRequest();
        final DeliveryAddress deliveryAddress = new DeliveryAddress("first", "last");
        deliveryAddress.setAddress1("address1");
        deliveryAddress.setAddress2("address1");
        deliveryAddress.setCity("London");
        deliveryAddress.setPostalCode("EC4V3BJ");
        deliveryAddress.setCountryCode(CountryCode.GB);
        orderRequest.setDeliveryAddress(deliveryAddress);
        final String emailAddress = "email@test.com";
        orderRequest.setShopperEmailAddress(emailAddress);
        orderRequest.setToken(tokenResponse.getToken());

        return aExcisingOrder(orderRequest);
    }

    private OrderRequest createOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAmount(1999);
        orderRequest.setCurrencyCode(CurrencyCode.GBP);
        orderRequest.setName("test name");
        orderRequest.setOrderDescription("test description");
        orderRequest.setBillingAddress(createAddress());

        List<Entry> customerIdentifiers = new ArrayList<Entry>();
        Entry entry = new Entry("test key 1", "test value 1");
        customerIdentifiers.add(entry);

        orderRequest.setCustomerIdentifiers(customerIdentifiers);
        return orderRequest;
    }

    private Address createAddress(){
        Address address = new Address();
        address.setAddress1("line 1");
        address.setAddress2("line 2");
        address.setCity("city");
        address.setCountryCode(CountryCode.GB);
        address.setPostalCode("AB1 2CD");

        return address;
    }
}
