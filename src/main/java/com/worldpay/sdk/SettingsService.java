package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.common.RiskSetting;
import com.worldpay.gateway.clearwater.client.ui.dto.response.SettingResponse;

/**
 * Service used for the settings related operations.
 */
public class SettingsService extends AbstractService {

    private final String MERCHANT_SETTINGS = "/merchants/%s/settings";

    private final String MERCHANT_SETTINGS_RISK = "/merchants/%s/settings/riskSettings";

    private final String MERCHANT_SETTINGS_RECURRING_BILLING = "/merchants/%s/settings/orderSettings/recurringBilling/%b";

    /**
     * Constructor
     *
     * @param http {@link Http}
     */
    protected SettingsService(Http http) {
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
        return http.get(String.format(MERCHANT_SETTINGS, merchantId), SettingResponse.class);
    }

    /**
     * Update the risking settings
     *
     * @param merchantId String
     *
     * @param riskSettings {@link RiskSetting} object
     */
    public void updateRiskSettings(String merchantId, RiskSetting riskSettings) {
        http.put(String.format(MERCHANT_SETTINGS_RISK, merchantId), riskSettings);
    }

    /**
     * Update recurring billing settings
     *
     * @param merchantId String
     *
     * @param enable Boolean
     */
    public void updateRecurringBilling(String merchantId, Boolean enable) {
        http.put(String.format(MERCHANT_SETTINGS_RECURRING_BILLING, merchantId, enable), null);
    }
}
