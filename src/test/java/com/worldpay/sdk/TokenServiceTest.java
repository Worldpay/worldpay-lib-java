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
     * This test is for validating the Token API to retrieve the existing token.
     */
    @Test
    public void shouldGetValidToken() {
        TokenResponse responseToken = tokenService.get(PropertyUtils.getProperty("tokenId"));
        assertThat(responseToken.getToken(), is(notNullValue()));
        assertThat(responseToken.getPaymentMethod(), is(notNullValue()));
        assertThat("Contains the same token", PropertyUtils.getProperty("tokenId").equals(responseToken.getToken()));
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
}
