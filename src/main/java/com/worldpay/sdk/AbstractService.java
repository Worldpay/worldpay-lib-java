package com.worldpay.sdk;

/**
 * Parent service class.
 */
class AbstractService {

    protected Http http;

    AbstractService(Http http) {
        this.http = http;
    }

}
