package com.worldpay.sdk;

/**
 * Parent service class.
 */
class AbstractService {

    /**
     * Http component.
     */
    protected Http http;

    /**
     * Create a new service with the http connector.
     *
     * @param http
     */
    protected AbstractService(Http http) {
        this.http = http;
    }

}
