package com.worldpay.sdk.util;

import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
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
        /*String orderResponse =
            "{\n" + "  \"orderCode\" : \"3ceaac2a-4a8e-4c12-b5d9-855ce2547f20\",\n" + "  \"amount\" : 1200,\n"
            + "  \"currencyCode\" : \"EUR\",\n" + "  \"paymentStatus\" : \"SUCCESS\",\n" + "  \"paymentResponse\" : {\n"
            + "    \"type\" : \"ObfuscatedCard\",\n" + "    \"name\" : \"Shopper Name\",\n"
            + "    \"expiryMonth\" : 2,\n" + "    \"expiryYear\" : 2017,\n" + "    \"issueNumber\" : 1,\n"
            + "    \"startMonth\" : 1,\n" + "    \"startYear\" : 2011,\n" + "    \"cardType\" : \"MASTERCARD\",\n"
            + "    \"maskedCardNumber\" : \"**** **** **** 1111\"\n" + "  },\n"
            + "  \"customerOrderCode\" : \"CustomerOrderCode\"\n" + ", \"newPropertyToIgnore\":\"somevalue\"}";
        OrderResponse response = JsonParser.toObject(orderResponse, OrderResponse.class);
        assertThat("Response", response, not(nullValue()));

        orderResponse =
            "{\n" + "  \"orderCode\" : \"1fc06655-7874-4aac-afaf-5c568e2d8021\",\n" + "  \"amount\" : 1200,\n"
            + "  \"currencyCode\" : \"EUR\",\n" + "  \"paymentStatus\" : \"SUCCESS\",\n" + "  \"paymentResponse\" : {\n"
            + "    \"type\" : \"ObfuscatedCard\",\n" + "    \"name\" : \"Shopper Name\",\n"
            + "    \"expiryMonth\" : 2,\n" + "    \"expiryYear\" : 2017,\n" + "    \"issueNumber\" : 1,\n"
            + "    \"startMonth\" : 1,\n" + "    \"startYear\" : 2011,\n" + "    \"cardType\" : \"MASTERCARD\",\n"
            + "    \"maskedCardNumber\" : \"**** **** **** 1111\"\n" + "  },\n"
            + "  \"customerOrderCode\" : \"CustomerOrderCode\",\n" + "  \"disputes\" : [ {\n"
            + "    \"id\" : \"docId\",\n" + "    \"documentName\" : \"sampleName.txt\",\n"
            + "    \"creationDate\" : \"12/03/2015\"\n" + "  } ]\n" + "}";

        response = JsonParser.toObject(orderResponse, OrderResponse.class);
        assertThat("Response without additional properties", response, not(nullValue()));*/

        String json =
            " {\n \"token1111\" : \"valid_token\",\n \"paymentMethod\" : {\n \"type\" : \"ObfuscatedCard\",\n \"name\" : \"Shopper Name\",\n \"expiryMonth\" : 2,\n \"expiryYear\" : 2017,\n \"issueNumber\" : 1,\n \"startMonth\" : 1,\n \"startYear\" : 2011,\n \"cardType\" : \"MASTERCARD\",\n \"maskedCardNumber\" : \"**** **** **** 1111\"\n },\n \"reusable\" : true\n}";
        TokenResponse tr = JsonParser.toObject(json, TokenResponse.class);
        assertThat("Response without additional properties", tr, not(nullValue()));
    }
}
