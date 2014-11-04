package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.CountryCode;
import com.worldpay.gateway.clearwater.client.core.dto.CurrencyCode;
import com.worldpay.gateway.clearwater.client.core.dto.common.Address;
import com.worldpay.gateway.clearwater.client.core.dto.common.Entry;
import com.worldpay.gateway.clearwater.client.core.dto.common.OrderStatus;
import com.worldpay.gateway.clearwater.client.core.dto.request.CardRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.gateway.clearwater.client.ui.dto.common.SortDirection;
import com.worldpay.gateway.clearwater.client.ui.dto.common.SortProperty;
import com.worldpay.gateway.clearwater.client.ui.dto.order.Transaction;
import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.PropertyUtils;
import com.worldpay.sdk.util.WorldPayHttpHeaders;
import org.junit.Before;
import org.junit.Test;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderServiceTest {

    /**
     * Test Master card number.
     */
    private static final String TEST_MASTERCARD_NUMBER = "5555 5555 5555 4444";

    /**
     * JSON header value.
     */
    private static final String APPLICATION_JSON = "application/json";

    /**
     * Card Verification code.
     */
    private static final String TEST_CVC = "123";

    public static final String FROM_DATE = "2013-10-01";

    public static final String TO_DATE = "2014-11-01";

    public static final String ENVIRONMENT = "TEST";

    /**
     * Service under test
     */
    private OrderService orderService;

    @Before
    public void setup() {
        orderService = new WorldpayRestClient(PropertyUtils.serviceKey()).getOrderService();
    }

    @Test
    public void shouldCreateOrderForValidToken() {

        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());

        OrderResponse response = orderService.create(orderRequest);
        assertThat(response.getOrderCode(), is(notNullValue()));
        assertThat(response.getAmount(), is(1999));
        assertThat(response.getKeyValueResponse().getCustomerIdentifiers(), is(notNullValue()));

        assertThat("Response code", response.getOrderCode(), is(notNullValue()));
        assertThat("Amount", response.getAmount(), is(1999));
        assertThat("Customer identifier", response.getKeyValueResponse().getCustomerIdentifiers(), is(notNullValue()));
    }

    @Test
    public void shouldRefundOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());

        String orderCode = orderService.create(orderRequest).getOrderCode();
        assertThat("Order code", orderCode, is(notNullValue()));

        orderService.refund(orderCode);
    }

    @Test
    public void shouldPartialRefundOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());

        String orderCode = orderService.create(orderRequest).getOrderCode();
        assertThat("Order code", orderCode, is(notNullValue()));

        orderService.refund(orderCode, 1);
    }

    @Test
    public void shouldGetOrderForValidToken() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());
        String orderCode = orderService.create(orderRequest).getOrderCode();
        assertThat(orderCode, is(notNullValue()));
        Transaction response = orderService.getOrder(orderCode);
        assertThat("orders should be there", response, is(notNullValue()));
    }

    @Test
    public void shouldGetOrdersForValidToken() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());
        String orderCode = orderService.create(orderRequest).getOrderCode();
        assertThat(orderCode, is(notNullValue()));
        Object response = orderService
            .getOrders(PropertyUtils.getProperty("merchantId"), ENVIRONMENT, FROM_DATE, TO_DATE,
                       OrderStatus.SUCCESS.name(), 0, SortDirection.ASC.name(), SortProperty.CREATE_DATE.name(),
                       Boolean.FALSE);
        HashMap entry = (HashMap) response;
        List orders = (ArrayList) entry.get("orders");

        assertThat("orders should be there", orders, is(notNullValue()));
        assertThat("number of orders should be more than one", orders.size(), is(greaterThan(0)));
    }

    @Test
    public void shouldThrowExceptionForInvalidToken() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken("invalid-token");
        try {
            orderService.create(orderRequest);
        } catch (WorldpayException e) {
            assertThat("Valid token", e.getApiError().getCustomCode(), is("TKN_NOT_FOUND"));
        }
    }


    private OrderRequest createOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAmount(1999);
        orderRequest.setCurrencyCode(CurrencyCode.GBP);
        orderRequest.setName("test name");
        orderRequest.setOrderDescription("test description");

        Address address = new Address();
        address.setAddress1("line 1");
        address.setAddress2("line 2");
        address.setCity("city");
        address.setCountryCode(CountryCode.GB);
        address.setPostalCode("AB1 2CD");
        orderRequest.setBillingAddress(address);

        List<Entry> customerIdentifiers = new ArrayList<Entry>();
        Entry entry = new Entry("test key 1", "test value 1");
        customerIdentifiers.add(entry);

        orderRequest.setCustomerIdentifiers(customerIdentifiers);
        return orderRequest;
    }

    private String createToken() {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(TEST_MASTERCARD_NUMBER);
        cardRequest.setCvc(TEST_CVC);
        cardRequest.setName("javalib client");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(2018);

        tokenRequest.setPaymentMethod(cardRequest);

        final String json = JsonParser.toJson(tokenRequest);

        String fullUri = PropertyUtils.getProperty("tokenUrl");
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        try {
            url = new URL(fullUri);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty(WorldPayHttpHeaders.ACCEPT, APPLICATION_JSON);
            httpURLConnection.setRequestProperty(WorldPayHttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(json);
            dataOutputStream.flush();
            dataOutputStream.close();
            TokenResponse tokenResponse = JsonParser.toObject(httpURLConnection.getInputStream(), TokenResponse.class);

            return tokenResponse.getToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
