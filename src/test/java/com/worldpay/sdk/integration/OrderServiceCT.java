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

import com.tngtech.jgiven.annotation.As;
import com.tngtech.jgiven.junit.ScenarioTest;
import com.worldpay.gateway.clearwater.client.core.dto.CountryCode;
import com.worldpay.gateway.clearwater.client.core.dto.CurrencyCode;
import com.worldpay.gateway.clearwater.client.core.dto.common.Address;
import com.worldpay.gateway.clearwater.client.core.dto.common.CommonToken;
import com.worldpay.gateway.clearwater.client.core.dto.common.DeliveryAddress;
import com.worldpay.gateway.clearwater.client.core.dto.common.Entry;
import com.worldpay.gateway.clearwater.client.core.dto.common.MerchantUrlConfig;
import com.worldpay.gateway.clearwater.client.core.dto.request.AlternatePaymentMethod;
import com.worldpay.gateway.clearwater.client.core.dto.request.CardRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderAuthorizationRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.ThreeDSecureInfo;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.sdk.util.HttpUrlConnection;
import com.worldpay.sdk.util.JsonParser;
import com.worldpay.sdk.util.PropertyUtils;
import org.junit.Test;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceCT extends ScenarioTest<OrderServiceClientStage, OrderStage, AssertOfferResponseStage> {

    /**
     * Test Master card number.
     */
    private static final String TEST_MASTERCARD_NUMBER = "5555 5555 5555 4444";

    /**
     * Card Verification code.
     */
    private static final String TEST_CVC = "123";

    /**
     * Apm Name
     */
    private static final String APM_NAME = "paypal";

    /**
     * Success url
     */
    private static final String SUCCESS_URL = "http://www.wp.com/success";

    /**
     * Cancel url
     */
    private static final String CANCEL_URL = "http://www.wp.com/cancel";

    /**
     * Failure url
     */
    private static final String FAILURE_URL = "http://www.wp.com/failure";

    /**
     * Pending url
     */
    private static final String PENDING_URL = "http://www.wp.com/pending";

    /**
     * Property name for a service key which can be used for order site routing
     */
    private static final String PROPERTY_SERVICE_KEY_SITE = "serviceKey_site";

    /**
     * Property name for a client key which can be use for order site routing
     */
    private static final String PROPERTY_CLIENT_KEY_SITE = "clientKey_site";

    /**
     * This test for creating an order with valid token
     */
    @Test
    public void shouldCreateOrderForValidToken() {
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
        orderRequest.setToken(createToken());

        when()
            .wePostAnOrderRequest(orderRequest);

        then()
            .theOrderCodeIsNotNull()
            .and()
            .theAmountIs(1999)
            .and()
            .theCustomerIdentifiersIsNotNull()
            .and()
            .theCardTypeIs("MASTERCARD_CREDIT")
            .and()
            .theDeliveryAddressIs(deliveryAddress)
            .and()
            .theShopperEmailAddressIs(emailAddress);
    }

    /**
     * Test for creating 3DS order with valid token and 3DS information.
     */
    @Test
    @As("Should create order for valid token and 3DS")
    public void shouldCreateOrderForValidTokenAndThreeDS() {
        OrderRequest orderRequest = createOrderRequestWithThreeDS();
        orderRequest.setToken(createToken());

        when()
            .wePostAnOrderRequest(orderRequest);

        then()
            .theOrderCodeIsNotNull()
            .and()
            .theAmountIs(1999)
            .and()
            .theCustomerIdentifiersIsNotNull();
    }

    /**
     * Test for creating order with given site code for order routing.
     */
    @Test
    public void shouldCreateOrderWithSiteCode() {
        final String siteCode = "NEW";
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken(PropertyUtils.getProperty(PROPERTY_CLIENT_KEY_SITE)));
        orderRequest.setSiteCode(siteCode);
        orderRequest.setSettlementCurrency(CurrencyCode.USD);

        given()
            .aWorldpayRestClientWithServiceKey(PropertyUtils.getProperty(PROPERTY_SERVICE_KEY_SITE));

        when()
            .wePostAnOrderRequest(orderRequest);

        then()
            .theResponseIsNotNull()
            .and()
            .theOrderCodeIsNotNull()
            .and()
            .theCurrencyCodeIs(CurrencyCode.USD);
    }

    /**
     * This is the test for creating the 3D Order.
     * This test expects authorize3Ds to return {@link OrderResponse} and order status should be Success.
     */
    @Test
    @As(" Should authorize 3DS order")
    public void shouldAuthorizeThreeDSOrder() {
        OrderRequest orderRequest = createOrderRequestWithThreeDS();
        orderRequest.setName("3D");
        orderRequest.setToken(createToken());

        OrderAuthorizationRequest orderAuthorizationRequest =
            createOrderAuthorizationRequest(orderRequest.getThreeDSecureInfo(), "IDENTIFIED");

        given()
            .aExcisingOrder(orderRequest);

        when()
            .weAuthorizeTheOrder(orderAuthorizationRequest);

        then()
            .theResponseIsNotNull()
            .and()
            .theOrderCodeIsTheSame()
            .and()
            .thePaymentStatusIsSuccess();
    }

    /**
     * This is the test for testing 3DS order with invalid 3DS relevant information.
     */
    @Test
    @As("should throw exception if 3DS is enabled but the info is invalid")
    public void shouldThrowExceptionIfThreeDSEnabledButInfoInvalid() {
        OrderRequest orderRequest = createOrderRequestWithThreeDS();
        orderRequest.setThreeDSecureInfo(null);
        orderRequest.setToken(createToken());

        when()
            .wePostAnOrderRequest(orderRequest);

        then()
            .aWorldpayExceptionIsThrown();
    }

    /**
     * This is the test for testing alternate payment methods.
     */
    @Test
    public void shouldCreateAlternatePaymentMethodOrderWithValidToken() {
        OrderRequest orderRequest = createOrderRequestWithAPM();
        orderRequest.setToken(createApmToken());

        when()
           .wePostAnOrderRequest(orderRequest);

        then()
            .theOrderCodeIsNotNull()
            .and()
            .theAmountIs(1999)
            .and()
            .theCustomerIdentifiersIsNotNull()
            .and()
            .theRedirectUrlIsNotNull();
    }

    @Test
    public void shouldTokenlessCardOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.getCommonToken().setPaymentMethod(createCardRequest());

        when()
            .wePostAnOrderRequest(orderRequest);

        then()
            .theOrderCodeIsNotNull()
            .and()
            .thePaymentStatusIsSuccess();
    }

    @Test
    public void shouldTokenlessApmOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.getCommonToken().setPaymentMethod(createAlternatePaymentMethod());

        when()
            .wePostAnOrderRequest(orderRequest);

        then()
            .theOrderCodeIsNotNull()
            .and()
            .theRedirectUrlContainsTheOrderCode();
    }

    /**
     * This is the test for full refund an order
     */
    @Test
    public void shouldRefundOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());

        given()
            .aExcisingOrder(orderRequest);

        when()
            .weRefundTheOrder();
    }

    /**
     * This is the test for partial refund an order
     */
    @Test
    public void shouldPartialRefundOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());

        given()
            .aExcisingOrder(orderRequest);

        when()
            .weRefundTheOrder(1);
    }

    /**
     * This is the test for creating an order with invalid token.
     * Expects API error
     */
    @Test
    public void shouldThrowExceptionForInvalidToken() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken("invalid-token");

        when()
            .wePostAnOrderRequest(orderRequest);

        then()
            .theApiErrorCustomCodeIs("TKN_NOT_FOUND");
    }

    /**
     * This is the test for creating the authorize only Order.
     * This test expects create to return {@link OrderResponse} and order status should be AUTHORIZED.
     */
    @Test
    public void shouldAuthorizeOnlyOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());
        orderRequest.setAuthorizeOnly(Boolean.TRUE);

        when()
            .wePostAnOrderRequest(orderRequest);

        then()
            .theOrderCodeIsNotNull()
            .and()
            .theAmountIs(0)
            .and()
            .thePaymentStatusIsAuthorized();
    }

    /**
     * This is the test for cancelling the authorize only Order.
     * This test expects create to return {@link OrderResponse} and order status should be CANCELLED.
     */
    @Test
    public void shouldCancelAuthorizeOnlyOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());
        orderRequest.setAuthorizeOnly(Boolean.TRUE);

        given()
            .aExcisingOrder(orderRequest);

        when()
            .weCancelTheOrder()
            .and()
            .weFindTheOrder();

        then()
            .theTransactionIsNotNull()
            .and()
            .theOrderResponseIsNotNull()
            .and()
            .thePaymentStatusInTheOrderResponseIsCancelled();
    }

    /**
     * This is the test for partial capture the authorize only Order.
     * This test expects create to return {@link OrderResponse} and order status should be SUCCESS.
     */
    @Test
    public void shouldPartialCaptureAuthorizeOnlyOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());
        orderRequest.setAuthorizeOnly(Boolean.TRUE);

        given()
            .aExcisingOrder(orderRequest)
            .and()
            .wePartialCaptureTheOrder(900);

        when()
            .weFindTheOrder();

        then()
            .theTransactionIsNotNull()
            .and()
            .theOrderResponseIsNotNull()
            .and()
            .thePaymentStatusInTheOrderResponseIsSuccess()
            .and()
            .theAmountInTheOrderResponseIs(900)
            .and()
            .theAuthorizedAmountInTheOrderResponseIs(1999);
    }

    /**
     * This is the test for full capture the authorize only Order.
     * This test expects create to return {@link OrderResponse} and order status should be SUCCESS.
     */
    @Test
    public void shouldFullCaptureAuthorizeOnlyOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());
        orderRequest.setAuthorizeOnly(Boolean.TRUE);

        given()
            .aExcisingOrder(orderRequest)
            .thatIsAuthorizeOnly()
            .and()
            .weCaptureTheOrder();

        when()
            .weFindTheOrder();

        then()
            .theTransactionIsNotNull()
            .and()
            .theOrderResponseIsNotNull()
            .and()
            .thePaymentStatusInTheOrderResponseIsSuccess()
            .and()
            .theAmountInTheOrderResponseIs(1999)
            .and()
            .theAuthorizedAmountInTheOrderResponseIs(1999);
    }

    /**
     * This is the test for over capture the authorize only Order.
     * This test expects API error.
     */
    @Test
    public void shouldExcessCaptureAuthorizeOnlyOrder() {
        OrderRequest orderRequest = createOrderRequest();
        orderRequest.setToken(createToken());
        orderRequest.setAuthorizeOnly(Boolean.TRUE);

        given()
            .aExcisingOrder(orderRequest);

        when()
            .wePartialCaptureTheOrder(2000);

        then()
            .aWorldpayExceptionIsThrown()
            .and()
            .theErrorMessageIs("API error: Capture amount cannot be more than authorized order amount");
    }

    @Test
    public void shouldCreateAndUseToken() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("4444333322221111");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(2017);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, false);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        given()
            .weCreateAToken(tokenRequest)
            .weCreateAnOrder();

        when()
            .weFindTheOrder();

        then()
            .theTransactionIsNotNull()
            .and()
            .theOrderResponseIsNotNull()
            .and()
            .thePaymentStatusInTheOrderResponseIsSuccess();
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

    /**
     * Create a token
     *
     * @return token
     */
    private String createToken() {
        return createToken(PropertyUtils.getProperty("clientKey"));
    }

    /**
     * Create a token with given client key.
     *
     * @param clientKey the client key
     *
     * @return token string
     */
    private String createToken(String clientKey) {
        TokenRequest tokenRequest = new TokenRequest(createCardRequest(), false);
        tokenRequest.setClientKey(clientKey);

        return getToken(tokenRequest);
    }

    private CardRequest createCardRequest() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(TEST_MASTERCARD_NUMBER);
        cardRequest.setCvc(TEST_CVC);
        cardRequest.setName("javalib client");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(2018);
        return cardRequest;
    }

    /**
     * Create a token for an alternate payment method.
     *
     * @return Alternate Payment Method Token
     */
    private String createApmToken() {
        AlternatePaymentMethod alternatePaymentMethod = createAlternatePaymentMethod();
        TokenRequest tokenRequest = new TokenRequest(alternatePaymentMethod, false);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        return getToken(tokenRequest);
    }

    private AlternatePaymentMethod createAlternatePaymentMethod() {
        AlternatePaymentMethod alternatePaymentMethod = new AlternatePaymentMethod();
        alternatePaymentMethod.setApmName(APM_NAME);
        alternatePaymentMethod.setShopperCountryCode(CountryCode.GB);

        Map<String, String> apmFields = new HashMap<String, String>();
        apmFields.put("bankId", "some value");
        apmFields.put("someOtherId", "some value");

        alternatePaymentMethod.setApmFields(apmFields);
        return alternatePaymentMethod;
    }

    /**
     * Post request to fetch token.
     *
     * @param tokenRequest the request to get a token
     *
     * @return token value
     */
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
