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

package com.worldpay.gateway.clearwater.client.core.exception;

import com.worldpay.gateway.clearwater.client.core.dto.ApiError;

/**
 * This exception wraps a {@link ApiError}.
 */
public class WorldpayException extends RuntimeException {

    /**
     * Serial version id
     */
    private static final long serialVersionUID = -2133902866208985142L;

    /**
     * Api error
     */
    private ApiError apiError;

    /**
     * Create a new exception
     *
     * @param error   an instance of {@link ApiError}
     * @param message exception message
     */
    public WorldpayException(ApiError error, String message) {
        super(message);
        this.apiError = error;
    }

    /**
     * Create a new exception
     *
     * @param message exception message
     */
    public WorldpayException(String message) {
        super(message);
    }

    public ApiError getApiError() {
        return apiError;
    }
}
