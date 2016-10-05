package com.worldpay.sdk.integration;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.worldpay.gateway.clearwater.client.core.dto.response.CardResponse;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.sdk.util.PropertyUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

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

    public AssertTokenResponseStage theTokenStartsWith(String tokenPrefix) {
        assertThat(tokenResponse.getToken(), startsWith(tokenPrefix));
        return self();
    }

    public AssertTokenResponseStage theCardTypeIs(String cardType) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getCardType(), is(cardType));
        return self();
    }

    public AssertTokenResponseStage theMaskedCardNumberIs(String cardNumber) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getMaskedCardNumber(), is(cardNumber));
        return self();
    }

    public AssertTokenResponseStage theCardSchemaTypeIs(String cardSchemaType) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getCardDetail().getCardSchemeType(), is(cardSchemaType));
        return self();
    }

    public AssertTokenResponseStage theCardSchemaNameIs(String cardSchemaName) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getCardDetail().getCardSchemeName(), is(cardSchemaName));
        return self();
    }

    public AssertTokenResponseStage theCardIssuerIs(String cardIssuer) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getCardDetail().getCardIssuer(), is(cardIssuer));
        return self();
    }

    public AssertTokenResponseStage theCountryCodeIs(String countryCode) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getCardDetail().getCountryCode(), is(countryCode));
        return self();
    }

    public AssertTokenResponseStage theCardClassIs(String cardClass) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getCardDetail().getCardClass(), is(cardClass));
        return self();
    }

    public AssertTokenResponseStage theCardProductTypeDescNonContactless(String cardProductType) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getCardDetail().getCardProductTypeDescNonContactless(), is(cardProductType));
        return self();
    }

    public AssertTokenResponseStage theCardProductTypeDescContactless(String cardProductType) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getCardDetail().getCardProductTypeDescContactless(), is(cardProductType));
        return self();
    }

    public AssertTokenResponseStage thePrepaidIs(String prepaid) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getCardDetail().getPrepaid(), is(prepaid));
        return self();
    }

    public AssertTokenResponseStage theExpiryMonthIs(int expityMonth) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getExpiryMonth(), is(expityMonth));
        return self();
    }

    public AssertTokenResponseStage theExpiryYearIs(int expityYear) {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getExpiryYear(), is(expityYear));
        return self();
    }

    public AssertTokenResponseStage theIssuerNumberIsNull() {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getIssueNumber(), is(nullValue()));
        return self();
    }

    public AssertTokenResponseStage theStartMonthIsNull() {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getStartMonth(), is(nullValue()));
        return self();
    }

    public AssertTokenResponseStage theStartYearIsNull() {
        assertThat(((CardResponse) tokenResponse.getCommonToken().getPaymentMethod()).getStartYear(), is(nullValue()));
        return self();
    }

    public AssertTokenResponseStage theNameIs(String name) {
        assertThat(tokenResponse.getCommonToken().getPaymentMethod().getName(), is(name));
        return self();
    }

    public AssertTokenResponseStage theBillingAddressIsNull() {
        assertThat(tokenResponse.getCommonToken().getPaymentMethod().getBillingAddress(), is(nullValue()));
        return self();
    }

    public AssertTokenResponseStage theTokenIsNotReusable() {
        assertThat(tokenResponse.getCommonToken().isReusable(), is(false));
        return self();
    }

    public AssertTokenResponseStage theShopperLanguageCodeIsNull() {
        assertThat(tokenResponse.getCommonToken().getShopperLanguageCode(), is(nullValue()));
        return self();
    }
}
