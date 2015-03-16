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

import com.worldpay.sdk.util.PropertyUtils;

/**
 * Main interface for interacting with the Worldpay Payment Gateway REST service.<br><br>
 *
 * Example:
 *
 * <pre>
 *
 *   WorldpayRestClient restClient = new WorldpayRestClient("YOUR_SERVICE_KEY");
 *
 *   OrderRequest orderRequest = new OrderRequest();
 *   orderRequest.setToken("valid-token");
 *   orderRequest.setAmount(1999);
 *   orderRequest.setCurrencyCode(CurrencyCode.GBP);
 *   orderRequest.setName("test name");
 *   orderRequest.setOrderDescription("test description");
 *
 *   try {
 *       OrderResponse orderResponse = restClient.getOrderService().create(orderRequest);
 *       System.out.println("Order code: " + orderResponse.getOrderCode());
 *   } catch (WorldpayException e) {
 *       System.out.println("Error code: " + e.getError().getCustomCode());
 *       System.out.println("Error description: " + e.getError().getDescription());
 *       System.out.println("Error message: " + e.getError().getMessage());
 *   }
 * </pre>
 */
public class WorldpayRestClient {

    /**
     * Http.
     */
    private Http http;

    /**
     * Create a new client with the specified base URL and the service key.
     *
     * @param baseUrl URL for connecting to the service, cannot be null
     * @param serviceKey the service key for authentication, cannot be null
     */
    public WorldpayRestClient(String baseUrl, String serviceKey) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("baseUrl cannot be null");
        }

        if (serviceKey == null) {
            throw new IllegalArgumentException("serviceKey cannot be null");
        }

        this.http = new Http(baseUrl, serviceKey);
    }

    /**
     * Create a new client with the specified service key and default connection.
     *
     * @param serviceKey the service key for authentication, cannot be null
     */
    public WorldpayRestClient(String serviceKey) {
        this(PropertyUtils.baseUrl(), serviceKey);
    }

    /**
     * Returns an {@link OrderService} for interacting with order end point.
     *
     * @return the order service
     */
    public OrderService getOrderService() {
        return new OrderService(http);
    }

    /**
     * Returns an {@link TokenService} for handling token requests
     *
     * @return the token service
     */
    public TokenService getTokenService() { return new TokenService(http); }
}
