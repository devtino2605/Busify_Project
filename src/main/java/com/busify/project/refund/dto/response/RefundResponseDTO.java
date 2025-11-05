package com.busify.project.refund.dto.response;

import com.busify.project.refund.enums.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponseDTO {

    private Long refundId;
    private Long paymentId;
    private BigDecimal refundAmount;
    private BigDecimal cancellationFee;
    private BigDecimal netRefundAmount;
    private String refundReason;
    private RefundStatus status;
    private String refundTransactionCode;
    private String gatewayRefundId;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private LocalDateTime completedAt;
    private String notes;
}
