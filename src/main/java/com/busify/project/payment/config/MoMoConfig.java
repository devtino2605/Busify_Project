package com.busify.project.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * MoMoConfig
 * 
 * Configuration for MoMo Payment Gateway
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-12-01
 */
@Configuration
@Getter
public class MoMoConfig {

    @Value("${momo.partner-code}")
    private String partnerCode;

    @Value("${momo.access-key}")
    private String accessKey;

    @Value("${momo.secret-key}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    @Value("${momo.ipn-url}")
    private String ipnUrl;

    @Value("${momo.redirect-url}")
    private String redirectUrl;

    @Value("${momo.request-type}")
    private String requestType;

    /**
     * Get create payment endpoint
     */
    public String getCreatePaymentUrl() {
        return endpoint + "/create";
    }

    /**
     * Get confirm payment endpoint
     */
    public String getConfirmPaymentUrl() {
        return endpoint + "/confirm";
    }
}
