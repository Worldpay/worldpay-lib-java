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

package com.worldpay.sdk.util;

import com.worldpay.gateway.clearwater.client.core.exception.WorldpayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUrlConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUrlConnection.class);

    public static HttpURLConnection getConnection(String fullUri) {
        HttpURLConnection httpURLConnection;
        URL url;
        try {
            url = new URL(fullUri);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setConnectTimeout(WorldpayLibraryConstants.CONNECTION_TIMEOUT);
            httpURLConnection.setReadTimeout(WorldpayLibraryConstants.SOCKET_TIMEOUT);
            httpURLConnection
                .setRequestProperty(WorldpayLibraryConstants.ACCEPT, WorldpayLibraryConstants.APPLICATION_JSON);
            httpURLConnection
                .setRequestProperty(WorldpayLibraryConstants.CONTENT_TYPE, WorldpayLibraryConstants.APPLICATION_JSON);
        } catch (IOException e) {
            LOGGER.error("Problem with the connection", e);
            throw new WorldpayException(e.getMessage());
        }
        return httpURLConnection;
    }
}
