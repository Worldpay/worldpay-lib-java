package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.common.RiskSetting;
import com.worldpay.gateway.clearwater.client.ui.dto.response.SettingResponse;

/**
 * Service used for the settings related operations.
 */
public class SettingsService extends AbstractService {

    SettingsService(Http http) {
        super(http);
    }

    /**
     * Return the active settings
     *
     * @param merchantId String
     *
     * @return {@link SettingResponse} object
     */
    public SettingResponse getSettings(String merchantId) {
        return http.get(String.format("/merchants/%s/settings", merchantId), SettingResponse.class);
    }

    /**
     * Update the risking settings
     *
     * @param merchantId String
     *
     * @param riskSettings {@link RiskSetting} object
     */
    public void updateRiskSettings(String merchantId, RiskSetting riskSettings) {
        http.put(String.format("/merchants/%s/settings/riskSettings", merchantId), riskSettings);
    }

    /**
     * Update recurring billing settings
     *
     * @param merchantId String
     *
     * @param enable Boolean
     */
    public void updateRecurringBilling(String merchantId, Boolean enable) {
        http.put(String.format("/merchants/%s/settings/orderSettings/recurringBilling/%b", merchantId, enable), null);
    }
}
