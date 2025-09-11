package com.busify.project.refund.strategy;

import com.busify.project.refund.dto.response.RefundResponseDTO;
import com.busify.project.refund.entity.Refund;

public interface RefundStrategy {

    /**
     * Process refund through payment gateway
     * 
     * @param refund Refund entity
     * @return RefundResponseDTO with updated status
     */
    RefundResponseDTO processRefund(Refund refund);

    /**
     * Check refund status from payment gateway
     * 
     * @param refund Refund entity
     * @return RefundResponseDTO with current status
     */
    RefundResponseDTO checkRefundStatus(Refund refund);

    /**
     * Check if this strategy supports the payment method
     * 
     * @param paymentMethod Payment method name
     * @return true if supported
     */
    boolean supports(String paymentMethod);
}
