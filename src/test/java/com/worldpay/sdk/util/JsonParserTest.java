package com.worldpay.sdk.util;

import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Test {@code JsonParser}
 */
public class JsonParserTest {

    @Test
    public void canParseToJson() {
        String json =
            " {\n \"token1111\" : \"valid_token\",\n \"paymentMethod\" : {\n \"type\" : \"ObfuscatedCard\",\n \"name\" : \"Shopper Name\",\n \"expiryMonth\" : 2,\n \"expiryYear\" : 2017,\n \"issueNumber\" : 1,\n \"startMonth\" : 1,\n \"startYear\" : 2011,\n \"cardType\" : \"MASTERCARD\",\n \"maskedCardNumber\" : \"**** **** **** 1111\"\n },\n \"reusable\" : true\n}";
        TokenResponse tr = JsonParser.toObject(json, TokenResponse.class);
        assertThat("Response without additional properties", tr, not(nullValue()));
    }
}
