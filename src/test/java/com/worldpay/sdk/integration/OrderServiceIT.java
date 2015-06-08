/*
 * Copyright 2013 Worldpay
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.worldpay.sdk.integration;

import com.worldpay.api.client.error.exception.WorldpayException;
import com.worldpay.gateway.clearwater.client.core.dto.CountryCode;
import com.worldpay.gateway.clearwater.client.core.dto.CurrencyCode;
import com.worldpay.gateway.clearwater.client.core.dto.common.Address;
import com.worldpay.gateway.clearwater.client.core.dto.common.Entry;
import com.worldpay.gateway.clearwater.client.core.dto.common.MerchantUrlConfig;
import com.worldpay.gateway.clearwater.client.core.dto.request.*;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.sdk.OrderService;
import com.worldpay.sdk.WorldpayRestClient;
import com.worldpay.sdk.util.HttpUrlConnection;
import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class OrderServiceIT {

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

    /**
     * Test OrderCode
     */
    private static final String TEST_ORDER_CODE = "orderCode";

    /**
     * Apm Name
     */
    private static final String APM_NAME = "paypal";

    /**
     * Success url
     */
    public static final String SUCCESS_URL = "http://www.wp.com/success";

    /**
     * Cancel url
     */
    public static final String CANCEL_URL = "http://www.wp.com/cancel";

    /**
     * Failure url
     */
    public static final String FAILURE_URL = "http://www.wp.com/failure";

    /**
     * Pending url
     */
    public static final String PENDING_URL = "http://www.wp.com/pending";

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

        assertThat("Response code", response.getOrderCode(), is(notNullValue()));
        assertThat("Amount", response.getAmount(), is(1999));
        assertThat("Customer identifier", response.getKeyValueResponse().getCustomerIdentifiers(), is(notNullValue()));
    }

    @Test
    public void shouldCreateOrderForValidTokenAndThreeDS() {

        OrderRequest orderRequest = createOrderRequestWithThreeDS();
        orderRequest.setToken(createToken());

        OrderResponse response = orderService.create(orderRequest);
        assertThat("Response code", response.getOrderCode(), is(notNullValue()));
        assertThat("Amount", response.getAmount(), is(1999));
        assertThat("Customer identifier", response.getKeyValueResponse().getCustomerIdentifiers(), is(notNullValue()));
    }

    @Test
    public  void shouldCreateOrderForValidTokenAndAlternatePaymentMethod(){

        OrderRequest orderRequest = createOrderRequestWithAPM();
        orderRequest.setToken(createApmToken());

        OrderResponse response = orderService.create(orderRequest);
        assertThat("Response code", response.getOrderCode(), is(notNullValue()));
        assertThat("Amount", response.getAmount(), is(1999));
        assertThat("Customer identifier", response.getKeyValueResponse().getCustomerIdentifiers(), is(notNullValue()));
        assertThat("Redirect URL", response.getRedirectURL(), is(notNullValue()));
    }

    @Test(expected = WorldpayException.class)
    public void shouldThrowExceptionIfThreeDSEnabledButInfoInvalid() {

        OrderRequest orderRequest = createOrderRequestWithThreeDS();
        orderRequest.setThreeDSecureInfo(null);
        orderRequest.setToken(createToken());

        OrderResponse response = orderService.create(orderRequest);
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
    public void shouldThrowExceptionForInvalidToken() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken("invalid-token");
        try {
            orderService.create(orderRequest);
        } catch (WorldpayException e) {
            assertThat("Valid token", e.getApiError().getCustomCode(), is("TKN_NOT_FOUND"));
        }
    }


    /**
     * Create an order request with three DS enabled
     *
     * @return {@link OrderRequest}
     */
    private OrderRequest createOrderRequestWithThreeDS() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setIs3DSOrder(true);

        ThreeDSecureInfo threeDSecureInfo = createThreeDsSecureInfo();
        orderRequest.setThreeDSecureInfo(threeDSecureInfo);

        return orderRequest;
    }

    /**
     * Create an order request with an APM
     *
     * @return {@link OrderRequest}
     */
    private OrderRequest createOrderRequestWithAPM() {
        OrderRequest orderRequest = createOrderRequest();

        MerchantUrlConfig merchantUrlConfig = new MerchantUrlConfig();
        merchantUrlConfig.setSuccessUrl(SUCCESS_URL);
        merchantUrlConfig.setCancelUrl(CANCEL_URL);
        merchantUrlConfig.setFailureUrl(FAILURE_URL);
        merchantUrlConfig.setPendingUrl(PENDING_URL);
        orderRequest.setMerchantUrlConfig(merchantUrlConfig);

        return orderRequest;
    }

    /**
     * Create a test ThreeDSecureInfo
     *
     * @return the test ThreeDSecureInfo
     */
    private ThreeDSecureInfo createThreeDsSecureInfo() {
        ThreeDSecureInfo threeDSecureInfo = new ThreeDSecureInfo();
        threeDSecureInfo.setShopperAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        threeDSecureInfo.setShopperIpAddress("195.35.90.111");
        threeDSecureInfo.setShopperSessionId("021ui8ib1");
        threeDSecureInfo.setShopperUserAgent(
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.1.5) Gecko/20091102 Firefox/3.5.5 (.NET CLR 3.5.30729)");
        return threeDSecureInfo;
    }

    /**
     * Create a test OrderAuthorizationRequest
     *
     * @return return the test OrderAuthorizationRequest
     */
    private OrderAuthorizationRequest createOrderAuthorizationRequest(ThreeDSecureInfo threeDSecureInfo,
                                                                      String threeDsResponseCode) {
        OrderAuthorizationRequest orderAuthorizationRequest = new OrderAuthorizationRequest();
        orderAuthorizationRequest.setThreeDSResponseCode(threeDsResponseCode);
        orderAuthorizationRequest.setThreeDSecureInfo(threeDSecureInfo);
        return orderAuthorizationRequest;
    }

    /**
     * Create a test OrderRequest
     *
     * @return {@link OrderRequest}
     */
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

        return getToken(tokenRequest);
    }

    private String createApmToken() {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));
        //tokenRequest.setClientKey("L_C_53344c8a-851c-40ed-acfd-356862d480c0");

        AlternatePaymentMethod alternatePaymentMethod = new AlternatePaymentMethod();
        alternatePaymentMethod.setApmName(APM_NAME);
        alternatePaymentMethod.setShopperCountryCode(CountryCode.GB);

        Map<String, String> apmFields = new HashMap<String, String>();
        apmFields.put("bankId", "some value");
        apmFields.put("someOtherId", "some value");

        //alternatePaymentMethod.setApmFields(apmFields);

        tokenRequest.setPaymentMethod(alternatePaymentMethod);
        return getToken(tokenRequest);
    }

    private String getToken(TokenRequest tokenRequest) {
        final String json = JsonParser.toJson(tokenRequest);

        String fullUri = PropertyUtils.getProperty("tokenUrl");
        HttpURLConnection httpURLConnection = HttpUrlConnection.getConnection(fullUri);
        try {
            httpURLConnection.setRequestMethod("POST");
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(json);

            TokenResponse tokenResponse = JsonParser.toObject(httpURLConnection.getInputStream(), TokenResponse.class);

            return tokenResponse.getToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
