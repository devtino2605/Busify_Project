package com.busify.project.refund.strategy.impl;

import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.refund.dto.response.RefundResponseDTO;
import com.busify.project.refund.entity.Refund;
import com.busify.project.refund.enums.RefundStatus;
import com.busify.project.refund.repository.RefundRepository;
import com.busify.project.refund.strategy.RefundStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * SimulationRefundStrategy
 * 
 * Refund strategy for testing/demo purposes
 * Automatically approves refund without any gateway
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-12-01
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SimulationRefundStrategy implements RefundStrategy {

    private final RefundRepository refundRepository;

    @Override
    public RefundResponseDTO processRefund(Refund refund) {
        try {
            log.info("Processing simulation refund for refund ID: {}", refund.getRefundId());

            // Generate fake gateway refund ID
            String simulationRefundId = "SIMREF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Auto-complete refund immediately for simulation
            refund.setStatus(RefundStatus.COMPLETED);
            refund.setCompletedAt(LocalDateTime.now());
            refund.setProcessedAt(LocalDateTime.now());
            refund.setGatewayRefundId(simulationRefundId);
            refund.setGatewayResponse("Simulation refund auto-approved");

            // Update payment status to refunded
            refund.getPayment().setStatus(PaymentStatus.refunded);

            refund = refundRepository.save(refund);

            log.info("Simulation refund completed immediately for refund ID: {}, gateway refund ID: {}",
                    refund.getRefundId(), simulationRefundId);

            return mapToDTO(refund);

        } catch (Exception e) {
            log.error("Error processing simulation refund for refund ID: {}", refund.getRefundId(), e);

            refund.setStatus(RefundStatus.FAILED);
            refund.setProcessedAt(LocalDateTime.now());
            refund.setNotes("Error: " + e.getMessage());
            refund = refundRepository.save(refund);

            return mapToDTO(refund);
        }
    }

    @Override
    public RefundResponseDTO checkRefundStatus(Refund refund) {
        try {
            log.info("Checking simulation refund status for refund ID: {}", refund.getRefundId());

            // For simulation, status is always what we set (completed or failed)
            return mapToDTO(refund);

        } catch (Exception e) {
            log.error("Error checking simulation refund status for refund ID: {}", refund.getRefundId(), e);
            return mapToDTO(refund);
        }
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.SIMULATION.name().equals(paymentMethod);
    }

    /**
     * Map Refund entity to DTO
     */
    private RefundResponseDTO mapToDTO(Refund refund) {
        return RefundResponseDTO.builder()
                .refundId(refund.getRefundId())
                .paymentId(refund.getPayment().getPaymentId())
                .refundAmount(refund.getRefundAmount())
                .cancellationFee(refund.getCancellationFee())
                .netRefundAmount(refund.getNetRefundAmount())
                .refundReason(refund.getRefundReason())
                .requestedAt(refund.getRequestedAt())
                .status(refund.getStatus())
                .processedAt(refund.getProcessedAt())
                .completedAt(refund.getCompletedAt())
                .gatewayRefundId(refund.getGatewayRefundId())
                .notes(refund.getNotes())
                .build();
    }
}
