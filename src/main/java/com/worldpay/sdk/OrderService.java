package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.RefundOrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.ui.dto.order.Transaction;

/**
 * Service used for the order related operations.
 */
public class OrderService extends AbstractService {

    OrderService(Http http) {
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
        return http.post("/orders", orderRequest, OrderResponse.class);
    }

    /**
     * Refund the order identified by order code.
     *
     * @param orderCode Order code
     */
    public void refund(String orderCode) {
        http.post("/orders/" + orderCode + "/refund", null);
    }

    /**
     * Partially Refund the order identified by order code by the amount given
     *
     * @param orderCode the order to be refunded
     * @param amount    the amount to be refunded
     */
    public void refund(String orderCode, int amount) {
        RefundOrderRequest refundRequest =  new RefundOrderRequest(amount);
        http.post("/orders/" + orderCode + "/refund", refundRequest);
    }

    public Transaction get(String orderCode) {
        return http.get("/orders/" + orderCode, Transaction.class);
    }
}
