package com.worldpay.sdk.integration;

import com.tngtech.jgiven.junit.ScenarioTest;
import com.worldpay.gateway.clearwater.client.core.dto.common.CommonToken;
import com.worldpay.gateway.clearwater.client.core.dto.request.CardRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.sdk.util.PropertyUtils;
import org.junit.Test;

import java.util.Calendar;

import static java.util.Calendar.YEAR;

public class TokenServiceCT extends ScenarioTest<EmptyStage, TokenStage, AssertTokenResponseStage> {

    private static final int EXPIRY_YEAR;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.add(YEAR, 1);
        EXPIRY_YEAR = calendar.get(YEAR);
    }

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
    public void shouldCreateReusableTokenForVisa() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("4444333322221111");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, true);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theTokenStartsWith("TEST_RU_")
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
            .theExpiryYearIs(EXPIRY_YEAR)
            .and()
            .theIssuerNumberIsNull()
            .and()
            .theStartMonthIsNull()
            .and()
            .theStartYearIsNull()
            .and()
            .theNameIs("John Doe")
            .and()
            .theTokenIsReusable()
            .and()
            .theShopperLanguageCodeIsNull()
            .and()
            .theBillingAddressIsNull();
    }

    @Test
    public void shouldCreateReusableTokenForMaestro() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("6759649826438453");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, true);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theTokenStartsWith("TEST_RU_")
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
            .theExpiryYearIs(EXPIRY_YEAR)
            .and()
            .theIssuerNumberIsNull()
            .and()
            .theStartMonthIsNull()
            .and()
            .theStartYearIsNull()
            .and()
            .theNameIs("John Doe")
            .and()
            .theTokenIsReusable()
            .and()
            .theShopperLanguageCodeIsNull()
            .and()
            .theBillingAddressIsNull();
    }

    @Test
    public void shouldCreateReusableTokenForAmex() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("34343434343434");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, true);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKey"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theTokenStartsWith("TEST_RU_")
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
            .theExpiryYearIs(EXPIRY_YEAR)
            .and()
            .theIssuerNumberIsNull()
            .and()
            .theStartMonthIsNull()
            .and()
            .theStartYearIsNull()
            .and()
            .theNameIs("John Doe")
            .and()
            .theTokenIsReusable()
            .and()
            .theShopperLanguageCodeIsNull()
            .and()
            .theBillingAddressIsNull();
    }

    @Test
    public void shouldCreateSingleUseTokenForVisa() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("4444333322221111");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
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
            .theExpiryYearIs(EXPIRY_YEAR)
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
    public void shouldCreateSingleUseTokenForAmex() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("34343434343434");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
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
            .theExpiryYearIs(EXPIRY_YEAR)
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
    public void shouldCreateSingleUseTokenForMaestro() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("6759649826438453");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
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
            .theExpiryYearIs(EXPIRY_YEAR)
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
    public void shouldThrowExceptionIfTokenCreationRequestIsInvalid() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("6759649826438453");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
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

    @Test
    public void shouldCreateSingleUseTokenWithLiveClientApiKey() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("4444333322221111");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, false);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKeyLive"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theTokenStartsWith("LIVE_SU_")
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
            .theExpiryYearIs(EXPIRY_YEAR)
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
    public void shouldCreateReusableTokenWithLiveClientApiKey() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("4444333322221111");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, true);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKeyLive"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theTokenStartsWith("LIVE_RU_")
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
            .theExpiryYearIs(EXPIRY_YEAR)
            .and()
            .theIssuerNumberIsNull()
            .and()
            .theStartMonthIsNull()
            .and()
            .theStartYearIsNull()
            .and()
            .theNameIs("John Doe")
            .and()
            .theTokenIsReusable()
            .and()
            .theShopperLanguageCodeIsNull()
            .and()
            .theBillingAddressIsNull();
    }

    @Test
    public void shouldCreateReuseableTokenStoreTokenWithLiveClientApiKey() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setName("John Doe");
        cardRequest.setCardNumber("4444333322221111");
        cardRequest.setExpiryMonth(2);
        cardRequest.setExpiryYear(EXPIRY_YEAR);
        cardRequest.setCvc("123");

        CommonToken commonToken = new CommonToken(cardRequest, true);

        TokenRequest tokenRequest = new TokenRequest(commonToken);
        tokenRequest.setClientKey(PropertyUtils.getProperty("clientKeyWpg"));

        when()
            .weCreateAToken(tokenRequest);

        then()
            .theTokenIsNotNull()
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
            .theExpiryYearIs(EXPIRY_YEAR)
            .and()
            .theIssuerNumberIsNull()
            .and()
            .theStartMonthIsNull()
            .and()
            .theStartYearIsNull()
            .and()
            .theNameIs("John Doe")
            .and()
            .theTokenIsReusable()
            .and()
            .theShopperLanguageCodeIsNull()
            .and()
            .theBillingAddressIsNull();
    }
}
