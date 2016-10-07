package com.worldpay.sdk.util;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.net.HttpURLConnection;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.worldpay.sdk.util.HttpUrlConnection.getConnection;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HttpUrlConnectionTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Test
    public void httpUrlConnectionIsConfigured61sConnectionTimeout() throws Exception {
        HttpURLConnection httpURLConnection = getConnection("http://localhost:" + wireMockConfig().portNumber());
        assertThat(httpURLConnection.getConnectTimeout(), is(equalTo(61000)));
        httpURLConnection.disconnect();
    }

    @Test
    public void httpUrlConnectionIsConfigured61sReadTimeout() throws Exception {
        HttpURLConnection httpURLConnection = getConnection("http://localhost:" + wireMockConfig().portNumber());
        assertThat(httpURLConnection.getReadTimeout(), is(equalTo(61000)));
        httpURLConnection.disconnect();
    }
}