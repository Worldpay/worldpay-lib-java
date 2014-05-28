Worldpay REST API Java SDK
=====================

Java SDK for interacting with the Worldpay Developer REST API.

## Usage

Initialize by providing the service key and then use the required service:
```java	
	WorldpayRestClient restClient = new WorldpayRestClient("YOUR_SERVICE_KEY");
	
	OrderRequest orderRequest = new OrderRequest();
	orderRequest.setSingleUseToken("valid-token");
	orderRequest.setAmount(1999);
	orderRequest.setCurrencyCode(CurrencyCode.GBP);
	orderRequest.setName("test name");
	orderRequest.setOrderDescription("test description");
	
	try {
	    OrderResponse orderResponse = restClient.order().create(orderRequest);
	    System.out.println("Order code: " + orderResponse.getOrderCode());
	} catch (ClearwaterException e) {
	    System.out.println("Error code: " + e.getError().getCustomCode());
	    System.out.println("Error description: " + e.getError().getDescription());
	    System.out.println("Error message: " + e.getError().getMessage());
	}
```