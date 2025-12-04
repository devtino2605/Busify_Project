package com.busify.project.payment.strategy.impl;

import com.busify.project.common.event.PaymentSuccessEvent;
import com.busify.project.common.publisher.BusifyEventPublisher;
import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.repository.PaymentRepository;
import com.busify.project.payment.strategy.PaymentStrategy;
import com.busify.project.ticket.service.TicketService;
import com.busify.project.trip_seat.services.SeatReleaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * SimulationPaymentStrategy
 * 
 * Payment strategy for testing/demo purposes
 * Automatically approves payment without any gateway
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-12-01
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SimulationPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final BusifyEventPublisher eventPublisher;
    private final SeatReleaseService seatReleaseService;
    private final TicketService ticketService;

    @Override
    public String createPaymentUrl(Payment paymentEntity, PaymentRequestDTO paymentRequest) {
        try {
            log.info("Creating simulation payment for payment ID: {}", paymentEntity.getPaymentId());

            // Generate fake payment gateway ID
            String simulationPaymentId = "SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            paymentEntity.setPaymentGatewayId(simulationPaymentId);

            // Auto-complete payment immediately for simulation
            paymentEntity.setStatus(PaymentStatus.completed);
            paymentEntity.setPaidAt(LocalDateTime.now());
            paymentEntity.setUpdatedAt(LocalDateTime.now());

            Payment savedPayment = paymentRepository.save(paymentEntity);

            // Handle post-payment actions
            handlePostPaymentActions(savedPayment);

            log.info("Simulation payment completed immediately for payment ID: {}, gateway ID: {}",
                    paymentEntity.getPaymentId(), simulationPaymentId);

            // Return null because payment is already completed (no redirect needed)
            // Frontend can check status and show success directly
            return null;

        } catch (Exception e) {
            log.error("Error creating simulation payment: ", e);
            throw new RuntimeException("Failed to create simulation payment", e);
        }
    }

    @Override
    public PaymentResponseDTO executePayment(Payment paymentEntity, String paymentId, String payerId) {
        try {
            log.info("Executing simulation payment for payment ID: {}", paymentEntity.getPaymentId());

            // Payment already completed in createPaymentUrl, just return success
            return PaymentResponseDTO.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.completed)
                    .bookingId(paymentEntity.isBooking() ? paymentEntity.getBooking().getId() : null)
                    .build();

        } catch (Exception e) {
            log.error("Error executing simulation payment: ", e);
            throw new RuntimeException("Failed to execute simulation payment", e);
        }
    }

    @Override
    public PaymentResponseDTO cancelPayment(Payment paymentEntity, String paymentId) {
        try {
            log.info("Cancelling simulation payment for payment ID: {}", paymentEntity.getPaymentId());

            paymentEntity.setStatus(PaymentStatus.cancelled);
            paymentEntity.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(paymentEntity);

            return PaymentResponseDTO.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.cancelled)
                    .build();

        } catch (Exception e) {
            log.error("Error cancelling simulation payment: ", e);
            throw new RuntimeException("Failed to cancel simulation payment", e);
        }
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.SIMULATION.name().equals(paymentMethod);
    }

    /**
     * Handle post-payment actions (seat release, event publishing, ticket creation,
     * email)
     */
    private void handlePostPaymentActions(Payment payment) {
        try {
            if (payment.isBooking()) {
                // Booking payment - complete flow like VNPay/PayPal
                Long bookingId = payment.getBooking().getId();

                // 1. Release seats from pending hold
                seatReleaseService.cancelReleaseTask(bookingId);

                // 2. Publish payment success event (for promotions)
                eventPublisher.publishEvent(
                        new PaymentSuccessEvent(this,
                                "Simulation payment successful for booking: " + bookingId,
                                payment));

                // 3. Create tickets (this also: updates booking status, updates seat status,
                // sends email)
                ticketService.createTicketsFromBooking(bookingId, null);

                log.info("Completed full payment flow for booking ID: {} - seats released, tickets created, email sent",
                        bookingId);

            } else if (payment.isCargo()) {
                // Cargo payment - just publish event
                // Email will be sent when cargo status is updated to CONFIRMED (after
                // inspection)
                eventPublisher.publishEvent(
                        new PaymentSuccessEvent(this,
                                "Simulation cargo payment successful for cargo: "
                                        + payment.getCargoBooking().getCargoCode(),
                                payment));

                log.info("Published event for cargo booking: {}", payment.getCargoBooking().getCargoCode());
            }

        } catch (Exception e) {
            log.error("Error handling post-payment actions: ", e);
            // Don't throw exception, payment is already completed
        }
    }
}
