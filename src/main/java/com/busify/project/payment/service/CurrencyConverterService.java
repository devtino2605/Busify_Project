package com.busify.project.payment.service;

import java.math.BigDecimal;

public interface CurrencyConverterService {

    /**
     * Convert VND to USD
     * 
     * @param vndAmount Amount in VND
     * @return Amount in USD
     */
    BigDecimal convertVndToUsd(BigDecimal vndAmount);

    /**
     * Convert USD to VND
     * 
     * @param usdAmount Amount in USD
     * @return Amount in VND
     */
    BigDecimal convertUsdToVnd(BigDecimal usdAmount);

    /**
     * Get current VND to USD exchange rate
     * 
     * @return Exchange rate (1 USD = X VND)
     */
    BigDecimal getCurrentExchangeRate();
}
