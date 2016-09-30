/*
 * Copyright 2013 Worldpay
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.request.TokenRequest;
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
    private static final String TOKENS_URL = "/tokens";

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
            throw new WorldpayException("token id should be empty");
        }
    }

    public TokenResponse create(TokenRequest token) {
        return http.post(TOKENS_URL + "/", token,  TokenResponse.class);
    }
}
