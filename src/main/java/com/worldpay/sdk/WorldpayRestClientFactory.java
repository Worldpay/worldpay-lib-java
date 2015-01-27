package com.worldpay.sdk;

/**
 * Test class to help test OrderController
 */
public class WorldpayRestClientFactory {

    /**
     * Create a new instance of WorldpayRestClient
     *
     * @param url        the eurl
     * @param serviceKey the security key
     *
     * @return a new WorldpayRestClient
     */
    public WorldpayRestClient createWorldpayRestClient(String url, String serviceKey) {
        return new WorldpayRestClient(url, serviceKey);
    }
}
