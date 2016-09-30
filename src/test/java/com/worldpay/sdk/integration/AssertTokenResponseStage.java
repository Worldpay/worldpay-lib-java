package com.worldpay.sdk.integration;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.sdk.util.PropertyUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AssertTokenResponseStage extends Stage<AssertTokenResponseStage> {

    @ExpectedScenarioState
    private TokenResponse tokenResponse;

    @ExpectedScenarioState
    private WorldpayException worldpayException;

    public AssertTokenResponseStage theTokenIsNotNull() {
        assertThat("Token Response", tokenResponse.getToken(), is(notNullValue()));

        return self();
    }

    public AssertTokenResponseStage thePaymentMethodIsNotNull() {
        assertThat("Payment Method", tokenResponse.getCommonToken().getPaymentMethod(), is(notNullValue()));
        return self();
    }

    public AssertTokenResponseStage theTokenIdIs(String tokenId) {
        assertThat("Contains the same token", PropertyUtils.getProperty("tokenId").equals(tokenId));
        return self();
    }

    public AssertTokenResponseStage theErrorMessageIs(String errorMessage) {
        assertThat("Invalid token", worldpayException.getMessage(), is(errorMessage));
        return self();
    }

    public AssertTokenResponseStage theApiErrorCustomCodeIs(String customCode) {
        assertThat("Invalid token", worldpayException.getApiError().getCustomCode(), is(customCode));
        return self();
    }
}
