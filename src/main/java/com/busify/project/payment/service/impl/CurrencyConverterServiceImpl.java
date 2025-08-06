package com.busify.project.payment.service.impl;

import com.busify.project.payment.service.CurrencyConverterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    // Tỷ giá cố định hoặc từ config (có thể extend để call API real-time)
    @Value("${payment.currency.usd-to-vnd-rate:24000}")
    private BigDecimal usdToVndRate;

    @Override
    public BigDecimal convertVndToUsd(BigDecimal vndAmount) {
        if (vndAmount == null || vndAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("VND amount must be positive");
        }

        BigDecimal usdAmount = vndAmount.divide(usdToVndRate, 2, RoundingMode.HALF_UP);
        log.info("Currency conversion: {} VND = {} USD (rate: {})",
                vndAmount, usdAmount, usdToVndRate);

        return usdAmount;
    }

    @Override
    public BigDecimal convertUsdToVnd(BigDecimal usdAmount) {
        if (usdAmount == null || usdAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("USD amount must be positive");
        }

        BigDecimal vndAmount = usdAmount.multiply(usdToVndRate)
                .setScale(0, RoundingMode.HALF_UP);
        log.info("Currency conversion: {} USD = {} VND (rate: {})",
                usdAmount, vndAmount, usdToVndRate);

        return vndAmount;
    }

    @Override
    public BigDecimal getCurrentExchangeRate() {
        return usdToVndRate;
    }
}
