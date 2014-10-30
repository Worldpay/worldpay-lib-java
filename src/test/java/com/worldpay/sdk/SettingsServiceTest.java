package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.response.SettingResponse;
import com.worldpay.sdk.util.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test the SettingsService interacts correctly with the API
 */
public class SettingsServiceTest {

    private SettingsService settingsService;

    private String merchantId;

    @Before
    public void setup() {
        settingsService = new WorldpayRestClient(PropertyUtils.serviceKey()).getSettingsService();
        merchantId = PropertyUtils.getProperty("merchantId");
    }

    @Test
    public void shouldReturnSettings() {
        SettingResponse settings = settingsService.getSettings(merchantId);
        assertThat("Settings", settings, is(notNullValue()));
    }

}
