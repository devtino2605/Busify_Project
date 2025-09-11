package com.busify.project.refund.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RefundPolicyUtil {

    // Refund policy constants
    private static final int REFUND_DEADLINE_HOURS = 24; // Không thể refund trong vòng 24h trước giờ khởi hành
    private static final BigDecimal EARLY_CANCELLATION_FEE_RATE = new BigDecimal("0.05"); // 5% phí hủy sớm
    private static final BigDecimal LATE_CANCELLATION_FEE_RATE = new BigDecimal("0.15"); // 15% phí hủy muộn
    private static final int EARLY_CANCELLATION_HOURS = 72; // Hủy trước 72h tính phí thấp

    /**
     * Kiểm tra có thể refund không dựa trên thời gian
     */
    public static boolean isRefundAllowed(Instant departureTime) {
        Instant now = Instant.now();
        long hoursUntilDeparture = ChronoUnit.HOURS.between(now, departureTime);

        return hoursUntilDeparture >= REFUND_DEADLINE_HOURS;
    }

    /**
     * Tính toán refund amount và cancellation fee theo điều kiện mới
     */
    public static RefundCalculation calculateRefund(BigDecimal originalAmount, Instant departureTime) {
        Instant now = Instant.now();
        long hoursUntilDeparture = ChronoUnit.HOURS.between(now, departureTime);

        BigDecimal refundPercentage;
        String refundReason;

        // Điều kiện refund mới theo yêu cầu
        if (hoursUntilDeparture <= 24) {
            refundPercentage = new BigDecimal("1.0"); // 100%
            refundReason = "Hủy trong vòng 24 giờ sau khi đặt";
        } else if (hoursUntilDeparture >= 24) {
            refundPercentage = new BigDecimal("0.7"); // 70%
            refundReason = "Hủy trước chuyến đi khoảng 1 ngày";
        } else {
            refundPercentage = new BigDecimal("0.0"); // 0%
            refundReason = "Hủy sát giờ khởi hành";
        }

        // Tính toán fee và net amount
        BigDecimal refundAmount = originalAmount.multiply(refundPercentage);
        BigDecimal cancellationFee = originalAmount.subtract(refundAmount);

        return new RefundCalculation(originalAmount, cancellationFee, refundAmount, hoursUntilDeparture);
    }

    /**
     * DTO chứa thông tin tính toán refund
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefundCalculation {
        private BigDecimal originalAmount;
        private BigDecimal cancellationFee;
        private BigDecimal netRefundAmount;
        private long hoursUntilDeparture;
    }
}
