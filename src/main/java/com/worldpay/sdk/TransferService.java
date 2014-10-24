package com.worldpay.sdk;

import com.worldpay.gateway.clearwater.client.ui.dto.response.TransferDetailResponse;
import com.worldpay.gateway.clearwater.client.ui.dto.response.TransferSearchResponse;

/**
 * Service used for transfer related operations
 */
public class TransferService extends AbstractService {

    /**
     * Transfer
     */
    private String TRANSFER = "/transfers/%s";

    /**
     * Transfer search
     */
    private String TRANSFER_SEARCH = "/transfers?merchantId=%s?pageNumber=%d";

    /**
     * Constructor
     *
     * @param http {@link Http}
     */
    protected TransferService(Http http) {
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
        return http.get(String.format(TRANSFER_SEARCH, merchantId, pageNumber), TransferSearchResponse.class);
    }

    /**
     * Return details of a single transfer
     *
     * @param transferId String
     *
     * @return {@link TransferDetailResponse} object
     */
    public TransferDetailResponse get(String transferId) {
        return http.get(String.format(TRANSFER, transferId), TransferDetailResponse.class);
    }
}
