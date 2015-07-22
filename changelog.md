Worldpay REST API Java SDK
=====================

#### 0.1.9
* New SDK Features:
* Capture an Authorize Only order using order code
* Cancel an Authorize Only order using Order Code, and amount to capture
* Suport for enhanced/additional card details
*
* Changed response for 3DS Authorize order API, from void to ``` OrderResponse``` :
* ``` public OrderResponse authorize3Ds(String orderCode, OrderAuthorizationRequest orderAuthorizationRequest) ```

#### 0.1.8
* New SDK Feature: getVersion API to provide the version of sdk at runtime
* Package structure changed for following for few response classes:
* ``` com.worldpay.gateway.clearwater.client.core.dto.ApiError ```
* ``` com.worldpay.gateway.clearwater.client.core.exception.WorldpayException ```
* ``` com.worldpay.gateway.clearwater.client.core.dto.CountryCode ```
* ``` com.worldpay.gateway.clearwater.client.core.dto.CurrencyCode ```

####  DEPRECATED

#### 0.1.7
* Upgrade to use latest features of CW
* Backward Compatibility issue


#### 0.1.6
* Upgrade to use latest features of CW
