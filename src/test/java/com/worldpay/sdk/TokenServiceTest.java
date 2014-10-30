package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.request.CardRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenUpdateRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.sdk.util.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TokenServiceTest {

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

    private TokenService tokenService;

    @Before
    public void setup() {
        tokenService = new WorldpayRestClient(PropertyUtils.serviceKey()).getTokenService();
    }

    @Test
    public void shouldCreateToken() {

        TokenRequest request = createTokenRequest();
        TokenResponse response = tokenService.create(request);

        assertThat(response.getToken(), is(notNullValue()));
        assertThat(response.getPaymentMethod(), is(notNullValue()));
    }

    @Test
    public void shouldDeleteValidToken() {

        TokenRequest request = createTokenRequest();
        TokenResponse response = tokenService.create(request);

        assertThat(response.getToken(), is(notNullValue()));
        assertThat(response.getPaymentMethod(), is(notNullValue()));

        tokenService.delete(response.getToken());
    }

    @Test
    public void shouldNotDeleteInValidToken() {
        try {
            tokenService.delete("invalid-token");
        } catch (WorldpayException e) {
            assertThat("Invalid token", e.getApiError().getCustomCode(), is("TKN_NOT_FOUND"));
        }
    }

    @Test
    public void shouldGetValidToken() {

        TokenRequest request = createTokenRequest();
        TokenResponse response = tokenService.create(request);

        assertThat(response.getToken(), is(notNullValue()));
        assertThat(response.getPaymentMethod(), is(notNullValue()));

        TokenResponse responseToken = tokenService.get(response.getToken());
        assertThat(responseToken.getToken(), is(notNullValue()));
        assertThat(responseToken.getPaymentMethod(), is(notNullValue()));
        assertThat("Contains the same token", response.getToken().equals(responseToken.getToken()));
    }

    @Test
    public void shouldNotGetInValidToken() {
        try {
            tokenService.get("invalid-token");
        } catch (WorldpayException e) {
            assertThat("Invalid token", e.getApiError().getCustomCode(), is("TKN_NOT_FOUND"));
        }
    }

    @Test
    public void shouldUpdateValidToken() {

        TokenRequest request = createTokenRequest();
        TokenResponse response = tokenService.create(request);

        assertThat(response.getToken(), is(notNullValue()));
        assertThat(response.getPaymentMethod(), is(notNullValue()));
        TokenUpdateRequest tokenUpdateRequest = new TokenUpdateRequest();
        tokenUpdateRequest.setCvc("321");
        tokenUpdateRequest.setClientKey(PropertyUtils.getProperty("clientKey"));
        tokenService.update(response.getToken(), tokenUpdateRequest);
    }



    private TokenRequest createTokenRequest() {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(TEST_MASTERCARD_NUMBER);
        cardRequest.setCvc(TEST_CVC);
        cardRequest.setName("javalib client");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(2018);

        tokenRequest.setPaymentMethod(cardRequest);

        return tokenRequest;
    }
}
