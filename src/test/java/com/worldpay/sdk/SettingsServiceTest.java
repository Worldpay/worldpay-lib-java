package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.common.RiskSetting;
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
        assertThat(settings, is(notNullValue()));
    }

    @Test
    public void shouldUpdateRecurringBillingSettings() {
        settingsService.updateRecurringBilling(merchantId, true);

        SettingResponse settings = settingsService.getSettings(merchantId);
        assertThat(settings, is(notNullValue()));
        assertThat(settings.getOrderSetting(), is(notNullValue()));
        assertThat(settings.getOrderSetting().isOptInForRecurringBilling(), is(true));

        settingsService.updateRecurringBilling(merchantId, false);

        settings = settingsService.getSettings(merchantId);
        assertThat(settings, is(notNullValue()));
        assertThat(settings.getOrderSetting().isOptInForRecurringBilling(), is(false));
    }

    @Test
    public void shouldUpdateRiskSettings() {
        RiskSetting risk = new RiskSetting(true, false);
        settingsService.updateRiskSettings(merchantId, risk);

        SettingResponse settings = settingsService.getSettings(merchantId);
        assertThat(settings, is(notNullValue()));
        assertThat(settings.getRiskSetting().isAvsEnabled(), is(true));
        assertThat(settings.getRiskSetting().isCvcEnabled(), is(false));

        risk = new RiskSetting(false, true);
        settingsService.updateRiskSettings(merchantId, risk);

        settings = settingsService.getSettings(merchantId);
        assertThat(settings, is(notNullValue()));
        assertThat(settings.getRiskSetting().isAvsEnabled(), is(false));
        assertThat(settings.getRiskSetting().isCvcEnabled(), is(true));
    }
}
