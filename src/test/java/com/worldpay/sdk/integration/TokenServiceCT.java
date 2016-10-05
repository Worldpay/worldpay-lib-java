package com.worldpay.sdk.integration;

import com.tngtech.jgiven.junit.ScenarioTest;
import com.worldpay.gateway.clearwater.client.core.dto.common.CommonToken;
import com.worldpay.gateway.clearwater.client.core.dto.request.CardRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.sdk.util.PropertyUtils;
import org.junit.Test;

public class TokenServiceCT extends ScenarioTest<EmptyStage, TokenStage, AssertTokenResponseStage> {

    /**
     * This test is for validating the Token API to retrieve the existing token.
     */
    @Test
    public void shouldGetValidToken() {
        when()
            .weGetAToken(PropertyUtils.getProperty("tokenId"));

        then()
            .theTokenIsNotNull()
            .and()
            .thePaymentMethodIsNotNull()
            .and()
            .theTokenIdIs(PropertyUtils.getProperty("tokenId"));
    }

    /**
     * This test is for validating the Token API to retrieve the non existing token.
     * The Token API throws WorldpayException with the custom code TKN_NOT_FOUND
     */
    @Test
    public void getTokenWithEmptyString() {
        when()
            .weGetAToken("");

        then()
            .theErrorMessageIs("token id should be empty");
    }

    /**
     * This test is for validating the Token API to retrieve the non existing token.
     * The Token API throws WorldpayException with the custom code TKN_NOT_FOUND
     */
    @Test
    public void shouldNotGetInvalidToken() {
        when()
            .weGetAToken("invalid-token");

        then()
            .theApiErrorCustomCodeIs("TKN_NOT_FOUND");
    }

    @Test
    public void shouldCreateASingleUseTokenForAVisaCard() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("4444333322221111");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(2017);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, false);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theTokenStartsWith("TEST_SU_")
            .and()
            .theCardTypeIs("VISA_CREDIT")
            .and()
            .theMaskedCardNumberIs("**** **** **** 1111")
            .and()
            .theCardSchemaTypeIs("consumer")
            .and()
            .theCardSchemaNameIs("VISA CREDIT")
            .and()
            .theCardIssuerIs("NATWEST")
            .and()
            .theCountryCodeIs("GB")
            .and()
            .theCardClassIs("credit")
            .and()
            .theCardProductTypeDescNonContactless("Visa Credit Personal")
            .and()
            .theCardProductTypeDescContactless("CL Visa Credit Pers")
            .and()
            .thePrepaidIs("false")
            .and()
            .theExpiryMonthIs(2)
            .and()
            .theExpiryYearIs(2017)
            .and()
            .theIssuerNumberIsNull()
            .and()
            .theStartMonthIsNull()
            .and()
            .theStartYearIsNull()
            .and()
            .theNameIs("John Doe")
            .and()
            .theTokenIsNotReusable()
            .and()
            .theShopperLanguageCodeIsNull()
            .and()
            .theBillingAddressIsNull();
    }

    @Test
    public void shouldCreateReusableTokenForAnAmexCard() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("34343434343434");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(2017);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, false);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theTokenStartsWith("TEST_SU_")
            .and()
            .theCardTypeIs("AMEX")
            .and()
            .theMaskedCardNumberIs("**** **** ** 3434")
            .and()
            .theCardSchemaTypeIs("unknown")
            .and()
            .theCardSchemaNameIs("unknown")
            .and()
            .theCardIssuerIs("unknown")
            .and()
            .theCountryCodeIs("XX")
            .and()
            .theCardClassIs("unknown")
            .and()
            .theCardProductTypeDescNonContactless("unknown")
            .and()
            .theCardProductTypeDescContactless("unknown")
            .and()
            .thePrepaidIs("unknown")
            .and()
            .theExpiryMonthIs(2)
            .and()
            .theExpiryYearIs(2017)
            .and()
            .theIssuerNumberIsNull()
            .and()
            .theStartMonthIsNull()
            .and()
            .theStartYearIsNull()
            .and()
            .theNameIs("John Doe")
            .and()
            .theTokenIsNotReusable()
            .and()
            .theShopperLanguageCodeIsNull()
            .and()
            .theBillingAddressIsNull();
    }

    @Test
    public void shouldCreateReusableTokenForAMaestroCard() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("6759649826438453");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(2017);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, false);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theTokenStartsWith("TEST_SU_")
            .and()
            .theCardTypeIs("MAESTRO")
            .and()
            .theMaskedCardNumberIs("**** **** **** 8453")
            .and()
            .theCardSchemaTypeIs("unknown")
            .and()
            .theCardSchemaNameIs("unknown")
            .and()
            .theCardIssuerIs("unknown")
            .and()
            .theCountryCodeIs("XX")
            .and()
            .theCardClassIs("unknown")
            .and()
            .theCardProductTypeDescNonContactless("unknown")
            .and()
            .theCardProductTypeDescContactless("unknown")
            .and()
            .thePrepaidIs("unknown")
            .and()
            .theExpiryMonthIs(2)
            .and()
            .theExpiryYearIs(2017)
            .and()
            .theIssuerNumberIsNull()
            .and()
            .theStartMonthIsNull()
            .and()
            .theStartYearIsNull()
            .and()
            .theNameIs("John Doe")
            .and()
            .theTokenIsNotReusable()
            .and()
            .theShopperLanguageCodeIsNull()
            .and()
            .theBillingAddressIsNull();
    }

    @Test
    public void shouldThrowExceptionIsTokenCreationRequestIsInvalid() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("6759649826438453");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(2017);
        cardRequest.setCvc("bad-cvc");

        CommonToken commonToken = new CommonToken(cardRequest, false);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theApiErrorCustomCodeIs("BAD_REQUEST")
            .theErrorMessageIs("API error: CVC Must always contain valid digits");
    }
}
