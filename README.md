Worldpay REST API Java SDK
=====================
Java SDK for interacting with the Worldpay Developer REST API.

#### Issues
Please see our [support contact information]( https://developer.worldpay.com/jsonapi/faq/articles/how-can-i-contact-you-for-support) to raise an issue.

#### Download

Please download lastest released jar from release section. This jar does not require any other dependency.

#### Documentation
https://online.worldpay.com/docs

#### API Reference
https://online.worldpay.com/api-reference

#### Usage

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
    System.out.println("Error code: " + e.getApiError().getCustomCode());
    System.out.println("Error description: " + e.getApiError().getDescription());
    System.out.println("Error message: " + e.getApiError().getMessage());
}
```

Alternatively, the client can also be initialized with the REST service URL as well as the service key e.g.
```java
WorldpayRestClient restClient = new WorldpayRestClient("https://api.worldpay.com/v1", "YOUR_SERVICE_KEY");
```

Use following API to get the Current SDK Library Version (available in v1.8 onwards only) in use:
```Java
WorldpayRestClient restClient = new WorldpayRestClient("YOUR_SERVICE_KEY");
String worldpaySdkVersion = restClient.getVersion();
```
