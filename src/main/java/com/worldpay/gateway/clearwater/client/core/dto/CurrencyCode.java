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

package com.worldpay.gateway.clearwater.client.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for currency code that we support
 */
public enum CurrencyCode {
    ALL(2),
    DZD(2),
    XCD(2),
    ARS(2),
    AWG(2),
    AUD(2),
    AZN(2),
    BSD(2),
    BHD(3),
    BDT(2),
    BBD(2),
    BZD(2),
    BMD(2),
    BOB(2),
    BWP(2),
    BRL(2),
    BND(2),
    BGN(2),
    XOF(0),
    MMK(2),
    KHR(2),
    XAF(0),
    CAD(2),
    KYD(2),
    CLP(2),
    CNY(2),
    COP(2),
    CRC(2),
    HRK(2),
    CZK(2),
    DKK(2),
    DJF(0),
    DOP(2),
    EGP(2),
    SVC(2),
    ERN(2),
    ETB(2),
    EUR(2),
    FJD(2),
    XPF(0),
    GEL(2),
    GHS(2),
    GIP(2),
    GTQ(2),
    HNL(2),
    HKD(2),
    HUF(2),
    ISK(2),
    INR(2),
    IDR(0),
    IRR(2),
    ILS(2),
    JMD(2),
    JPY(0),
    JOD(3),
    KZT(2),
    KES(2),
    KWD(3),
    LVL(2),
    LBP(2),
    LSL(2),
    LYD(3),
    LTL(2),
    MOP(2),
    MKD(2),
    MYR(2),
    MVR(2),
    MRO(2),
    MUR(2),
    MXN(2),
    MNT(2),
    MAD(2),
    MZN(2),
    NAD(2),
    ANG(2),
    NZD(2),
    NIO(2),
    NGN(2),
    NOK(2),
    OMR(3),
    PKR(2),
    PAB(2),
    PYG(0),
    PEN(2),
    PHP(2),
    PLN(2),
    QAR(2),
    RON(2),
    RUB(2),
    RWF(0),
    SAR(2),
    RSD(2),
    SCR(2),
    SLL(2),
    SGD(2),
    ZAR(2),
    KRW(0),
    LKR(2),
    SZL(2),
    SEK(2),
    CHF(2),
    SYP(2),
    TWD(2),
    TZS(2),
    THB(2),
    TTD(2),
    TND(3),
    TRY(2),
    UAH(2),
    AED(2),
    GBP(2),
    USD(2),
    UYU(2),
    UZS(2),
    VEF(2),
    VND(0),
    YER(2),
    ZMW(2);

    /**
     * Exponent
     */
    private final Integer exponent;

    /**
     * Static map for lookup later
     */
    private static final Map<String, CurrencyCode> CURRENCY_CODE_MAP = new HashMap<String, CurrencyCode>();

    /**
     * Builds map
     */
    static {
        for (CurrencyCode currencyCode : values()) {
            CURRENCY_CODE_MAP.put(currencyCode.name(), currencyCode);
        }
    }

    /**
     * Creates a token
     *
     * @param exponent exponent
     */
    private CurrencyCode(int exponent) {
        this.exponent = exponent;
    }

    public Integer getExponent() {
        return exponent;
    }

    /**
     * To support mixed case currency code
     *
     * @param value currency code
     *
     * @return {@code CurrencyCode}
     */
    @JsonCreator
    public static CurrencyCode fromValue(String value) {
        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException("Currency can't be null/empty");
        }
        String trimmedString = value.replaceAll("\\s+", "");
        return CURRENCY_CODE_MAP.get(trimmedString.toUpperCase());
    }
}
