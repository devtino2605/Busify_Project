package com.busify.project.payment.service;

import com.busify.project.payment.service.impl.CurrencyConverterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrencyConverterServiceTest {

    private CurrencyConverterServiceImpl currencyConverterService;

    @BeforeEach
    void setUp() {
        currencyConverterService = new CurrencyConverterServiceImpl();
        // Set test exchange rate: 1 USD = 24,000 VND
        ReflectionTestUtils.setField(currencyConverterService, "usdToVndRate", new BigDecimal("24000"));
    }

    @Test
    void convertVndToUsd_ShouldConvertCorrectly() {
        // Given: 240,000 VND
        BigDecimal vndAmount = new BigDecimal("240000");

        // When: Convert to USD
        BigDecimal usdAmount = currencyConverterService.convertVndToUsd(vndAmount);

        // Then: Should be 10.00 USD
        assertThat(usdAmount).isEqualByComparingTo("10.00");
    }

    @Test
    void convertUsdToVnd_ShouldConvertCorrectly() {
        // Given: 10.50 USD
        BigDecimal usdAmount = new BigDecimal("10.50");

        // When: Convert to VND
        BigDecimal vndAmount = currencyConverterService.convertUsdToVnd(usdAmount);

        // Then: Should be 252,000 VND
        assertThat(vndAmount).isEqualByComparingTo("252000");
    }

    @Test
    void convertVndToUsd_WithNullAmount_ShouldThrowException() {
        assertThatThrownBy(() -> currencyConverterService.convertVndToUsd(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("VND amount must be positive");
    }

    @Test
    void convertVndToUsd_WithZeroAmount_ShouldThrowException() {
        assertThatThrownBy(() -> currencyConverterService.convertVndToUsd(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("VND amount must be positive");
    }

    @Test
    void getCurrentExchangeRate_ShouldReturnConfiguredRate() {
        BigDecimal rate = currencyConverterService.getCurrentExchangeRate();
        assertThat(rate).isEqualByComparingTo("24000");
    }
}
