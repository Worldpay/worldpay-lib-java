package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;

/**
 * Service used for the token related operations.
 */
public class TokenService extends AbstractService {

    /**
     * url for token
     */
    private final String TOKENS_URL = "/tokens";

    /**
     * Constructor
     *
     * @param http {@link Http}
     */
    protected TokenService(Http http) {
        super(http);
    }

    /**
     * Retrieves the token information.
     *
     * @param token token id.
     *
     * @return instance of {@link TokenResponse} contains token information.
     */
    public TokenResponse get(String token) {
        return http.get(TOKENS_URL + "/" + token, TokenResponse.class);
    }
}
