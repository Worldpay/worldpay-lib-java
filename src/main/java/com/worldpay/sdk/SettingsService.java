package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.response.SettingResponse;

/**
 * Service used for the settings related operations.
 */
public class SettingsService extends AbstractService {

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
        final String uri = "/merchants/%s/settings";
        return http.get(String.format(uri, merchantId), SettingResponse.class);
    }
}
