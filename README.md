Worldpay REST API Java SDK
=====================

Java SDK for interacting with the Worldpay Developer REST API.

## Usage

Initialize the REST client with the default URL and the specified service key and then use the required service:
```Java
WorldpayRestClient restClient = new WorldpayRestClient("YOUR_SERVICE_KEY");

OrderRequest orderRequest = new OrderRequest();
orderRequest.setToken("valid-token");
orderRequest.setAmount(1999);
orderRequest.setCurrencyCode(CurrencyCode.GBP);
orderRequest.setName("test name");
orderRequest.setOrderDescription("test description");

try {
    OrderResponse orderResponse = restClient.getOrderService().create(orderRequest);
    System.out.println("Order code: " + orderResponse.getOrderCode());
} catch (WorldpayException e) {
    System.out.println("Error code: " + e.getError().getCustomCode());
    System.out.println("Error description: " + e.getError().getDescription());
    System.out.println("Error message: " + e.getError().getMessage());
}
```

Alternatively, the client can also be initialized with the REST service URL as well as the service key e.g.
```java
WorldpayGateway restClient = new WorldpayGateway("https://api.worldpay.com/v1", "YOUR_SERVICE_KEY");
```