package com.worldpay.sdk.integration;

import com.tngtech.jgiven.CurrentStep;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.sdk.TokenService;
import com.worldpay.sdk.WorldpayRestClient;
import com.worldpay.sdk.util.PropertyUtils;

import static com.tngtech.jgiven.attachment.Attachment.fromText;
import static com.tngtech.jgiven.attachment.MediaType.PLAIN_TEXT_UTF_8;

public class TokenStage extends Stage<TokenStage> {

    @ExpectedScenarioState
    private CurrentStep currentStep;

    private TokenService tokenService;

    @ProvidedScenarioState
    private TokenResponse tokenResponse;

    @ProvidedScenarioState
    private WorldpayException worldpayException;

    @BeforeStage
    public void init() {
        tokenService = new WorldpayRestClient(PropertyUtils.serviceKey()).getTokenService();
    }

    public TokenStage weGetAToken(String tokenId) {
        try {
            tokenResponse = tokenService.get(tokenId);
            currentStep.addAttachment(fromText(tokenResponse.toString(), PLAIN_TEXT_UTF_8).withTitle("Response"));
        } catch (WorldpayException e) {
            worldpayException = e;
        }
        return self();
    }

    public TokenStage weCreateAToken(TokenRequest tokenRequest) {
        try {
            tokenResponse = tokenService.create(tokenRequest);
            currentStep.addAttachment(fromText(tokenResponse.toString(), PLAIN_TEXT_UTF_8).withTitle("Response"));
        } catch (WorldpayException e) {
            worldpayException = e;
        }
        return self();
    }
}
