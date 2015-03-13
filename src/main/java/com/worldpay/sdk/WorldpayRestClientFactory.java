/*
 * Copyright 2013 Worldpay
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.worldpay.sdk;

/**
 * Test class to help test OrderController
 */
public class WorldpayRestClientFactory {

    /**
     * Create a new instance of WorldpayRestClient
     *
     * @param url        the url
     * @param serviceKey the security key
     *
     * @return a new WorldpayRestClient
     */
    public WorldpayRestClient createWorldpayRestClient(String url, String serviceKey) {
        return new WorldpayRestClient(url, serviceKey);
    }
}
