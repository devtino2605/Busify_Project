package com.busify.project.notification.service;

import java.util.logging.Logger;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.busify.project.common.event.PaymentSuccessEvent;
import com.busify.project.notification.controller.NotificationController;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationController notificationController;

    @EventListener
    public void handlePaymentSuccessEvent(PaymentSuccessEvent event) {
        Logger logger = Logger.getLogger(NotificationService.class.getName());
        logger.info(
                "Handling payment success event for payment ID: " + event.getPayment().getBooking().getGuestEmail());
        notificationController.notifyPaymentToOperator(
                event.getPayment().getBooking().getTrip().getBus().getOperator().getId(),
                event.getPayment().getBooking().getGuestEmail(),
                event.getPayment().getBooking().getGuestPhone());
    }
}
