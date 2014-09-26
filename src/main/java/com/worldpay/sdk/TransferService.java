package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.response.TransferDetailResponse;
import com.worldpay.gateway.clearwater.client.ui.dto.response.TransferSearchResponse;

/**
 * Created by MDS on 25/09/2014.
 */
public class TransferService extends AbstractService {

    TransferService(Http http) {
        super(http);
    }

    /**
     * Return a list of transfers for a given merchant
     *
     * @param merchantId String
     *
     * @param pageNumber Integer
     *
     * @return {@link TransferSearchResponse} object
     */
    public TransferSearchResponse search(String merchantId, Integer pageNumber) {
        return http.get(String.format("/transfers?merchantId=%s?pageNumber=%d", merchantId, pageNumber), TransferSearchResponse.class);
    }

    /**
     * Return details of a single transfer
     *
     * @param transferId String
     *
     * @return {@link TransferDetailResponse} object
     */
    public TransferDetailResponse get(String transferId) {
        return http.get(String.format("/transfers/%s", transferId), TransferDetailResponse.class);
    }
}
