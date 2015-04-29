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

package com.worldpay.gateway.clearwater.client.core.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * Error object that is send back to clients in case of an error.
 */
public class ApiError implements Serializable {

    private static final long serialVersionUID = 3946490182182240913L;

    /**
     * http internal server error
     */
    public static final int INTERNAL_SERVER_ERROR = 500;

    /**
     * Pattern to be looked for in Request to be logged, for replacement with hashed password
     */
    public static final String PASSWORD_MATCH_PATTERN_ONE = "\"(.*?)([pP])assword\":\"(.*?)\"";

    /**
     * String pattern to replace the password
     */
    public static final String PASSWORD_HASHED_ONE = "\"$1$2assword\":\"********\"";

    /**
     * Pattern to be looked for in Request to be logged, for replacement with hashed password
     */
    public static final String PASSWORD_MATCH_PATTERN_TWO = "'(.*?)([pP])assword\":\"(.*?)'";

    /**
     * String pattern to replace the password
     */
    public static final String PASSWORD_HASHED_TWO = "'$1$2assword':'********'";

    /**
     * Http status code
     */
    private Integer httpStatusCode;

    /**
     * Our custom code
     */
    private String customCode;

    /**
     * Brief Error message
     */
    private String message;

    /**
     * Description of error
     */
    private String description;

    /**
     * Link to errors and descriptions etc.
     * //TODO: For future
     */
    private String errorHelpUrl;

    /**
     * Original request that cause this error
     */
    private String originalRequest;

    /**
     * Default constructor
     */
    public ApiError() {
    }

    /**
     * Private Constructor of an api error, to be used only by {@link Builder}
     *
     * @param httpStatusCode http status code value
     * @param customCode     custom code
     * @param message        error message
     * @param description    error description
     * @param errorHelpUrl   error help url
     */
    private ApiError(Integer httpStatusCode, String customCode, String message, String description, String errorHelpUrl,
                     String originalRequest) {
        if (httpStatusCode == null) {
            this.httpStatusCode = INTERNAL_SERVER_ERROR;
        } else {
            this.httpStatusCode = httpStatusCode;
        }
        this.customCode = customCode;
        this.message = message;
        this.description = description;
        this.errorHelpUrl = errorHelpUrl;
        this.originalRequest = originalRequest;
    }

    public String getOriginalRequest() {
        return originalRequest;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getCustomCode() {
        return customCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public String getErrorHelpUrl() {
        return errorHelpUrl;
    }


    @Override
    public int hashCode() {
        return Objects.hash(httpStatusCode, customCode, message, description, errorHelpUrl);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ApiError other = (ApiError) obj;
        return Objects.equals(this.httpStatusCode, other.httpStatusCode) &&
               Objects.equals(this.customCode, other.customCode) &&
               Objects.equals(this.message, other.message) &&
               Objects.equals(this.description, other.description) &&
               Objects.equals(this.originalRequest, other.originalRequest) &&
               Objects.equals(this.errorHelpUrl, other.errorHelpUrl);
    }

    @Override
    public String toString() {
        return "ApiError{" +
               "httpStatusCode=" + httpStatusCode +
               ", customCode='" + customCode + '\'' +
               ", message='" + message + '\'' +
               ", description='" + description + '\'' +
               ", errorHelpUrl='" + errorHelpUrl + '\'' +
               '}';
    }

    /**
     * Builder class to build {@link ApiError}
     */
    public static class Builder {

        /**
         * Http status code
         */
        private Integer httpStatusCode;

        /**
         * Our custom code
         */
        private String customCode;

        /**
         * Brief Error message
         */
        private String message;

        /**
         * Description of error
         */
        private String description;

        /**
         * Link to errors and descriptions etc.
         * //TODO: For future
         */
        private String errorHelpUrl;

        /**
         * Original Json request
         */
        private String originalRequest;

        public Builder() {
        }

        public Builder setHttpStatusCode(Integer httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
            return this;
        }

        public Builder setCustomCode(String customCode) {
            this.customCode = customCode;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setErrorHelpUrl(String errorHelpUrl) {
            this.errorHelpUrl = errorHelpUrl;
            return this;
        }

        /**
         * hashes the password if present in the original request
         *
         * @param originalRequest Original request string
         *
         * @return {@link Builder} for method chaining
         */
        public Builder setOriginalRequest(String originalRequest) {
            if (originalRequest != null) {
                this.originalRequest = originalRequest.replaceAll(PASSWORD_MATCH_PATTERN_ONE, PASSWORD_HASHED_ONE);
                this.originalRequest = this.originalRequest.replaceAll(PASSWORD_MATCH_PATTERN_TWO, PASSWORD_HASHED_TWO);
            }
            return this;
        }

        /**
         * Build {@link ApiError}
         *
         * @return {@link ApiError}
         */
        public ApiError build() {
            if (this.httpStatusCode == null) {
                this.httpStatusCode = INTERNAL_SERVER_ERROR;
            }
            return new ApiError(this.httpStatusCode, this.customCode, this.message, this.description, this.errorHelpUrl,
                                this.originalRequest);
        }
    }
}
