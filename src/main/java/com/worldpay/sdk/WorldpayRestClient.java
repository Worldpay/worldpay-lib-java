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

}
