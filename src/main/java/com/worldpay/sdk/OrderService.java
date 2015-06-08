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

import com.worldpay.api.common.util.AssertUtils;
import com.worldpay.gateway.clearwater.client.core.dto.request.CaptureOrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderAuthorizationRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.RefundOrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.ui.dto.order.Transaction;

/**
 * Service used for the order related operations.
 */
public class OrderService extends AbstractService {

    /**
     * URL for CREATE order
     */
    private static final String ORDERS_URL = "/orders";

    /**
     * URL for Authorise 3DS order
     */
    private static final String AUTHORIZE_3DS_URL = "/orders/%s";

    /**
     * URL for REFUND ORDER
     */
    private static final String REFUND_URL = "/orders/%s/refund";

    /**
     * URL for REFUND ORDER
     */
    private static final String FIND_ORDER_URL = "/orders/%s";

    /**
     * URL for CANCEL ORDER
     */
    private static final String CANCEL_ORDER_URL = "/orders/%s";

    /**
     * URL for CAPTURE ORDER
     */
    private static final String CAPTURE_URL = "/orders/%s/capture";

    /**
     * Constructor
     *
     * @param http {@link Http}
     */
    protected OrderService(Http http) {
        super(http);
    }

    /**
     * Create an order with the specified request.
     *
     * @param orderRequest {@link OrderRequest} object
     *
     * @return {@link OrderResponse} object
     */
    public OrderResponse create(OrderRequest orderRequest) {
        return http.post(ORDERS_URL, orderRequest, OrderResponse.class);
    }

    /**
     * Refund the order identified by order code.
     *
     * @param orderCode Order code
     */
    public void refund(String orderCode) {
        AssertUtils.hasText(orderCode, "Order Code");
        http.post(String.format(REFUND_URL, orderCode), null);
    }

    /**
     * Find the order identified by order code.
     *
     * @param orderCode Order code
     *
     * @return {@link Transaction}
     */
    public Transaction findOrder(String orderCode) {
        AssertUtils.hasText(orderCode, "Order Code");
        return http.get(String.format(FIND_ORDER_URL, orderCode), Transaction.class);
    }

    /**
     * Authorize the given order using 3DS
     *
     * @param orderCode                 The order to authorize
     * @param orderAuthorizationRequest The request details
     *
     * @return {@link OrderResponse}
     */
    public OrderResponse authorize3Ds(String orderCode, OrderAuthorizationRequest orderAuthorizationRequest) {

        validateOrderAuthorizationRequest(orderAuthorizationRequest);
        return http.put(String.format(AUTHORIZE_3DS_URL, orderCode), orderAuthorizationRequest, OrderResponse.class);
    }

    /**
     * Partially Refund the order identified by order code by the amount given
     *
     * @param orderCode the order to be refunded
     * @param amount    the amount to be refunded
     */
    public void refund(String orderCode, int amount) {
        AssertUtils.hasText(orderCode, "Order Code");
        http.post(String.format(REFUND_URL, orderCode), new RefundOrderRequest(amount));
    }

    /**
     * Cancel the order identified by order code.
     *
     * @param orderCode Order code
     */
    public void cancel(String orderCode) {
        AssertUtils.hasText(orderCode, "Order Code");
        http.delete(String.format(CANCEL_ORDER_URL, orderCode), null);
    }

    /**
     * Capture the authorized amount from the order identified by order code.
     *
     * @param captureOrderRequest {@link CaptureOrderRequest}
     * @param orderCode           Order code
     *
     * @return {@link OrderResponse}
     */
    public OrderResponse capture(CaptureOrderRequest captureOrderRequest, String orderCode) {
        AssertUtils.hasText(orderCode, "Order Code");
        return http.post(String.format(CAPTURE_URL, orderCode), captureOrderRequest, OrderResponse.class);
    }

    /**
     * Validate a {@link OrderAuthorizationRequest}
     *
     * @param orderAuthorizationRequest The {@link OrderAuthorizationRequest} to be validated
     */
    private void validateOrderAuthorizationRequest(OrderAuthorizationRequest orderAuthorizationRequest) {

        AssertUtils.notNull(orderAuthorizationRequest, "Order Authorization Request");

        AssertUtils.hasText(orderAuthorizationRequest.getThreeDSResponseCode(), "Three DS Response Code");
        AssertUtils.notNull(orderAuthorizationRequest.getThreeDSecureInfo(), "Three DS Secure Info");
        AssertUtils
            .hasText(orderAuthorizationRequest.getThreeDSecureInfo().getShopperSessionId(), "Shopper Session Id");
        AssertUtils.hasText(orderAuthorizationRequest.getThreeDSecureInfo().getShopperAcceptHeader(),
                            "Shoppper Accept Header");
        AssertUtils
            .hasText(orderAuthorizationRequest.getThreeDSecureInfo().getShopperUserAgent(), "Shopper User Agent");
        AssertUtils
            .hasText(orderAuthorizationRequest.getThreeDSecureInfo().getShopperIpAddress(), "Shopper Ip Address");
    }
}
