package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenUpdateRequest;
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
     * Creates token for the given request.
     *
     * @param tokenRequest instance of {@link TokenRequest} contains information required for creating token .
     *
     * @return instance of {@link TokenResponse} contains token information.
     */
    public TokenResponse create(TokenRequest tokenRequest) {
        return http.post(TOKENS_URL, tokenRequest, TokenResponse.class);
    }

    /**
     * Deletes the token for the given token number.
     *
     * @param token token id.
     */
    public void delete(String token) {
        http.delete(TOKENS_URL + "/" + token);
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

    /**
     * Updates the token cvc information.
     *
     * @param token token id.
     *
     * @return instance of {@link TokenResponse} contains token information.
     */
    public void update(String token, TokenUpdateRequest tokenUpdateRequest) {
        http.put(TOKENS_URL + "/" + token, tokenUpdateRequest);
    }
}
