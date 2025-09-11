package com.busify.project.refund.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class RefundPolicyUtil {

    // Timezone mặc định cho hệ thống (có thể config từ application.properties)
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    /**
     * Convert Instant to LocalDateTime using system timezone
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return instant.atZone(DEFAULT_ZONE).toLocalDateTime();
    }

    /**
     * Convert LocalDateTime to Instant using system timezone
     */
    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(DEFAULT_ZONE).toInstant();
    }

    /**
     * Kiểm tra có thể refund không dựa trên thời gian (Instant version - primary
     * method)
     */
    public static boolean isRefundAllowed(Instant departureTime) {
        Instant now = Instant.now();
        long hoursUntilDeparture = ChronoUnit.HOURS.between(now, departureTime);

        // Cho phép refund nếu còn ít nhất 1 giờ trước giờ khởi hành
        // Hoặc nếu chuyến đi chưa khởi hành (hoursUntilDeparture > 0)
        return hoursUntilDeparture > 0;
    }

    /**
     * Kiểm tra có thể refund không dựa trên thời gian (LocalDateTime version -
     * helper method)
     */
    public static boolean isRefundAllowed(LocalDateTime departureTime) {
        return isRefundAllowed(toInstant(departureTime));
    }

    /**
     * Tính toán refund amount và cancellation fee (Instant version - primary
     * method)
     */
    public static RefundCalculation calculateRefund(BigDecimal originalAmount, Instant departureTime) {
        Instant now = Instant.now();
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
     * Tính toán refund amount và cancellation fee theo điều kiện mới (LocalDateTime
     * version - helper method)
     */
    public static RefundCalculation calculateRefund(BigDecimal originalAmount, LocalDateTime departureTime) {
        return calculateRefund(originalAmount, toInstant(departureTime));
    }

    /**
     * Lấy thời gian hiện tại (Instant)
     */
    public static Instant getCurrentTime() {
        return Instant.now();
    }

    /**
     * Lấy thời gian hiện tại theo timezone của hệ thống (LocalDateTime)
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(DEFAULT_ZONE);
    }

    /**
     * Tính toán số giờ từ hiện tại đến thời điểm khởi hành (Instant version -
     * primary)
     */
    public static long calculateHoursUntilDeparture(Instant departureTime) {
        Instant now = Instant.now();
        return ChronoUnit.HOURS.between(now, departureTime);
    }

    /**
     * Tính toán số giờ từ hiện tại đến thời điểm khởi hành (LocalDateTime version)
     */
    public static long calculateHoursUntilDeparture(LocalDateTime departureTime) {
        return calculateHoursUntilDeparture(toInstant(departureTime));
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
