package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import org.apache.commons.lang.StringUtils;

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
        validate(token);
        return http.get(TOKENS_URL + "/" + token, TokenResponse.class);
    }

    /**
     * Validates the order code. This method throws WorldpayException if the order code is null or empty.
     *
     * @param orderCode order code to be validated.
     */
    private void validate(String orderCode) {
        if (StringUtils.isEmpty(orderCode)) {
            throw new WorldpayException("order code should be empty");
        }
    }
}
