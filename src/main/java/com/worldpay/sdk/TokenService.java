package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.response.TokenResponse;
import com.worldpay.gateway.clearwater.service.core.exception.WpgException;
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
     * @param token token id to be validated.
     */
    private void validate(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new WpgException("token id should be empty", 1);
        }
    }
}
