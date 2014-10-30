package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.RefundOrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.TokenUpdateRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.client.ui.dto.order.Transaction;

/**
 * Service used for the token related operations.
 */
public class TokenService extends AbstractService {

    private final String TOKENS_URL = "/tokens";

    /**
     * Constructor
     *
     * @param http {@link Http}
     */
    protected TokenService(Http http) {
        super(http);
    }

    public TokenResponse create(TokenRequest tokenRequest) {
        return http.post(TOKENS_URL, tokenRequest, TokenResponse.class);
    }

    public void delete(String token) {
        http.delete(TOKENS_URL + "/" + token);
    }

    public TokenResponse get(String token) {
        return http.get(TOKENS_URL + "/" + token, TokenResponse.class);
    }

    public void update(String token, TokenUpdateRequest tokenUpdateRequest) {
        http.put(TOKENS_URL + "/" + token, tokenUpdateRequest);
    }
}
