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

/**
 * This is the test class for TokenService.
 */
public class TokenServiceTest {

    /**
     * Test Master card number.
     */
    private static final String TEST_MASTERCARD_NUMBER = "5555 5555 5555 4444";

    /**
     * Card Verification code.
     */
    private static final String TEST_CVC = "123";

    /**
     * Service under test
     */
    private TokenService tokenService;

    @Before
    public void setup() {
        tokenService = new WorldpayRestClient(PropertyUtils.serviceKey()).getTokenService();
    }

    /**
     * This test is for validating the Token API to create token.
     */
    @Test
    public void shouldCreateToken() {
        TokenRequest request = createTokenRequest();
        TokenResponse response = tokenService.create(request);

        assertThat(response.getToken(), is(notNullValue()));
        assertThat(response.getPaymentMethod(), is(notNullValue()));
    }

    /**
     * This test is for validating the Token API to delete the existing token
     */
    @Test
    public void shouldDeleteValidToken() {
        TokenRequest request = createTokenRequest();
        TokenResponse response = tokenService.create(request);

        assertThat(response.getToken(), is(notNullValue()));
        assertThat(response.getPaymentMethod(), is(notNullValue()));

        tokenService.delete(response.getToken());
    }

    /**
     * This test is for validating the Token API to delete the non existing token.
     * The Token API throws WorldpayException with the custom code TKN_NOT_FOUND
     */
    @Test
    public void shouldNotDeleteInValidToken() {
        try {
            tokenService.delete("invalid-token");
        } catch (WorldpayException e) {
            assertThat("Invalid token", e.getApiError().getCustomCode(), is("TKN_NOT_FOUND"));
        }
    }

    /**
     * This test is for validating the Token API to retrieve the existing token.
     */
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

    /**
     * This test is for validating the Token API to retrieve the non existing token.
     * The Token API throws WorldpayException with the custom code TKN_NOT_FOUND
     */
    @Test
    public void shouldNotGetInValidToken() {
        try {
            tokenService.get("invalid-token");
        } catch (WorldpayException e) {
            assertThat("Invalid token", e.getApiError().getCustomCode(), is("TKN_NOT_FOUND"));
        }
    }

    /**
     * This test is for validating the Token API to update the CVC.
     * The Token API throws WorldpayException with the custom code TKN_NOT_FOUND
     */
    @Test
    public void shouldUpdateCVCForValidToken() {
        TokenRequest request = createTokenRequest();
        TokenResponse response = tokenService.create(request);

        assertThat(response.getToken(), is(notNullValue()));
        assertThat(response.getPaymentMethod(), is(notNullValue()));

        TokenUpdateRequest tokenUpdateRequest = new TokenUpdateRequest();
        tokenUpdateRequest.setCvc("321");
        tokenUpdateRequest.setClientKey(PropertyUtils.getProperty("clientKey"));
        tokenService.update(response.getToken(), tokenUpdateRequest);
    }

    /**
     * This test is for validating the Token API to update the CVC.
     * The Token API throws WorldpayException with the custom code TKN_NOT_FOUND
     */
    @Test
    public void shouldUpdateCVCForInvalidToken() {
        TokenUpdateRequest tokenUpdateRequest = new TokenUpdateRequest();
        tokenUpdateRequest.setCvc("321");
        tokenUpdateRequest.setClientKey(PropertyUtils.getProperty("clientKey"));
        try {
            tokenService.update("invalid-token", tokenUpdateRequest);
        } catch (WorldpayException e) {
            assertThat("Invalid token", e.getApiError().getCustomCode(), is("TKN_NOT_FOUND"));
        }
    }

    private TokenRequest createTokenRequest() {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));
        tokenRequest.setPaymentMethod(createCardRequest());
        return tokenRequest;
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
}
