package com.busify.project.payment.service.impl;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.common.event.PaymentSuccessEvent;
import com.busify.project.payment.entity.Payment;
import com.busify.project.promotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessHandler {

    private final PromotionService promotionService;

    @EventListener
    @Transactional
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        Payment payment = event.getPayment();
        // If this payment is associated with a ticket booking, handle promotions
        if (payment.getBooking() != null) {
            Bookings booking = payment.getBooking();
            log.info("Handling payment success for booking ID: {}, payment ID: {}", booking.getId(),
                    payment.getPaymentId());

            // Mark promotion as used when payment is successful
            // Check both applied discount code (COUPON) and applied promotion ID (AUTO)
            try {
                // Mark COUPON promotion as used if discount code was applied
                if (booking.getAppliedDiscountCode() != null && !booking.getAppliedDiscountCode().trim().isEmpty()) {
                    if (booking.getCustomer() != null) {
                        promotionService.markPromotionAsUsed(booking.getCustomer().getId(),
                                booking.getAppliedDiscountCode());
                        log.info("Marked coupon promotion {} as used for user {} after successful payment",
                                booking.getAppliedDiscountCode(), booking.getCustomer().getId());
                    }
                }

                // Mark AUTO promotion as used if promotion ID was applied
                if (booking.getAppliedPromotionId() != null) {
                    if (booking.getCustomer() != null) {
                        promotionService.createAndMarkAutoPromotionAsUsed(booking.getCustomer().getId(),
                                booking.getAppliedPromotionId());
                        log.info("Marked auto promotion {} as used for user {} after successful payment",
                                booking.getAppliedPromotionId(), booking.getCustomer().getId());
                    }
                }
            } catch (Exception e) {
                log.error("Error marking promotion as used for booking {}: {}", booking.getId(), e.getMessage(), e);
                // Don't throw exception to avoid affecting payment success flow
            }
        } else {
            // It's a cargo payment - promotions are not applicable. Log and continue.
            log.info(
                    "PaymentSuccessHandler received a non-booking payment (paymentId={}), skipping booking-based promotion handling.",
                    payment.getPaymentId());
        }
    }
}