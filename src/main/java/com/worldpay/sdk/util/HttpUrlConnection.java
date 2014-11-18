package com.worldpay.sdk.util;

import com.worldpay.api.client.error.exception.WorldpayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUrlConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUrlConnection.class);

    public static HttpURLConnection getConnection(String fullUri) {
        HttpURLConnection httpURLConnection = null;
        URL url = null;
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
