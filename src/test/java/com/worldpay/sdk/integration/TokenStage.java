package com.worldpay.sdk.integration;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.BeforeStage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.sdk.TokenService;
import com.worldpay.sdk.WorldpayRestClient;
import com.worldpay.sdk.util.PropertyUtils;

public class TokenStage extends Stage<TokenStage> {

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
        } catch (WorldpayException e) {
            worldpayException = e;
        }
        return self();
    }
}
