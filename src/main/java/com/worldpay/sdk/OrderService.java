package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.core.dto.request.OrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.request.RefundOrderRequest;
import com.worldpay.gateway.clearwater.client.core.dto.response.OrderResponse;
import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import com.worldpay.gateway.clearwater.client.ui.dto.order.Transaction;
import org.apache.commons.lang.StringUtils;

/**
 * Service used for the order related operations.
 */
public class OrderService extends AbstractService {

    private final String ORDERS_URL = "/orders";

    private final String REFUND_URL = "/orders/%s/refund";

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
        validateOrder(orderCode);
        http.post(String.format(REFUND_URL, orderCode), null);
    }

    /**
     * Partially Refund the order identified by order code by the amount given
     *
     * @param orderCode the order to be refunded
     * @param amount    the amount to be refunded
     */
    public void refund(String orderCode, int amount) {
        validateOrder(orderCode);
        http.post(String.format(REFUND_URL, orderCode), new RefundOrderRequest(amount));
    }

    /**
     * Retrieve the order details by order code.
     *
     * @param orderCode the unique order code to be retrieved
     *
     * @return {@link Transaction} object
     */
    public Transaction getOrder(String orderCode) {
        validateOrder(orderCode);
        return http.get("/orders/" + orderCode, Transaction.class);
    }

    /**
     * Retrieves all the order details for the given criteria.
     * This method returns output either as in CSV format or as a POJO
     *
     * @param merchantId    unique merchant identifier
     * @param environment   environment is live/test
     * @param fromDate      all the orders with order date after this date will be retrieved
     * @param toDate        all the orders with order date before this date will be retrieved
     * @param paymentStatus payment status of the order
     * @param pageNumber    number of pages to be retrieved
     * @param sortDirection sort direction
     * @param sortProperty  sort property
     * @param csv           true for csv format
     *
     * @return instance of Object
     */
    public Object getOrders(String merchantId, String environment, String fromDate, String toDate, String paymentStatus,
                            Integer pageNumber, String sortDirection, String sortProperty, boolean csv) {
        return http.get(
            "/orders?merchantId=" + merchantId + "&environment=" + environment + "&fromDate=" + fromDate + "&toDate="
            + toDate + "&paymentStatus=" + paymentStatus + "&sortDirection=" + sortDirection + "&sortProperty="
            + sortProperty + "&pageNumber=" + pageNumber + "&csv=" + csv, Object.class);
    }

    /**
     * Validates the order code. This method throws WorldpayException if the order code is null or empty.
     *
     * @param orderCode order code to be validated.
     */
    private void validateOrder(String orderCode) {
        if (StringUtils.isEmpty(orderCode)) {
            throw new WorldpayException("order code should be empty");
        }
    }
}
