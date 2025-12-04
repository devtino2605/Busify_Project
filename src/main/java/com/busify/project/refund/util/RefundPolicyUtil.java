package com.busify.project.refund.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RefundPolicyUtil {

    /**
     * Kiểm tra có thể refund không dựa trên thời gian
     */
    public static boolean isRefundAllowed(LocalDateTime departureTime) {
        LocalDateTime now = LocalDateTime.now();
        long hoursUntilDeparture = ChronoUnit.HOURS.between(now, departureTime);

        // Cho phép refund nếu còn ít nhất 1 giờ trước giờ khởi hành
        // Hoặc nếu chuyến đi chưa khởi hành (hoursUntilDeparture > 0)
        return hoursUntilDeparture > 0;
    }

    /**
     * Tính toán refund amount và cancellation fee
     */
    public static RefundCalculation calculateRefund(BigDecimal originalAmount, LocalDateTime departureTime) {
        LocalDateTime now = LocalDateTime.now();
        long hoursUntilDeparture = ChronoUnit.HOURS.between(now, departureTime);

        BigDecimal refundPercentage;
        String refundReason;

        // Logic refund mới:
        // - Nếu chuyến đi đã khởi hành (hoursUntilDeparture <= 0): không refund
        // - Nếu hủy trong vòng 24h trước khởi hành: refund 70%
        // - Nếu hủy trước 24h: refund 100%

        if (hoursUntilDeparture <= 0) {
            refundPercentage = BigDecimal.ZERO; // 0%
            refundReason = "Chuyến đi đã khởi hành, không thể hoàn tiền";
        } else if (hoursUntilDeparture <= 24) {
            refundPercentage = new BigDecimal("0.7"); // 70%
            refundReason = "Hủy trong vòng 24 giờ trước khởi hành";
        } else {
            refundPercentage = new BigDecimal("1.0"); // 100%
            refundReason = "Hủy trước 24 giờ khởi hành";
        }

        // Tính toán fee và net amount
        BigDecimal refundAmount = originalAmount.multiply(refundPercentage);
        BigDecimal cancellationFee = originalAmount.subtract(refundAmount);

        return new RefundCalculation(originalAmount, cancellationFee, refundAmount, hoursUntilDeparture, refundReason);
    }

    /**
     * Lấy thời gian hiện tại
     */
    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    /**
     * Tính toán số giờ từ hiện tại đến thời điểm khởi hành
     */
    public static long calculateHoursUntilDeparture(LocalDateTime departureTime) {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.HOURS.between(now, departureTime);
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
        private String refundReason;

        // Constructor for backward compatibility
        public RefundCalculation(BigDecimal originalAmount, BigDecimal cancellationFee,
                BigDecimal netRefundAmount, long hoursUntilDeparture) {
            this.originalAmount = originalAmount;
            this.cancellationFee = cancellationFee;
            this.netRefundAmount = netRefundAmount;
            this.hoursUntilDeparture = hoursUntilDeparture;
        }
    }
}
