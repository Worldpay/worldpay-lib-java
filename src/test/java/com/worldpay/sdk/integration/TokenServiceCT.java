package com.worldpay.sdk.integration;

import com.tngtech.jgiven.junit.ScenarioTest;
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
    public void shouldNotGetInValidToken() {
        when()
            .weGetAToken("invalid-token");

        then()
            .theApiErrorCustomCodeIs("TKN_NOT_FOUND");
    }
}
