package com.busify.project.cargo.service;

import com.busify.project.cargo.entity.CargoBooking;
import com.busify.project.cargo.entity.CargoTracking;
import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.cargo.repository.CargoBookingRepository;
import com.busify.project.cargo.repository.CargoTrackingRepository;
import com.busify.project.payment.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CargoReleaseService
 * 
 * Service to handle automatic cancellation of unpaid cargo bookings
 * after 1 hour timeout period
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-13
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CargoReleaseService {

    private final CargoBookingRepository cargoBookingRepository;
    private final CargoTrackingRepository cargoTrackingRepository;

    /**
     * Recovery mechanism: Scan and cancel expired cargo bookings when server starts
     * This handles cases where server crashed before scheduled tasks could complete
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void recoverExpiredCargosOnStartup() {
        log.info("Starting recovery scan for expired unpaid cargo bookings...");

        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusHours(1);

            // Find cargo bookings that are PENDING and older than 1 hour
            List<CargoBooking> expiredCargos = cargoBookingRepository.findExpiredPendingCargoBookings(cutoffTime);

            log.info("Found {} expired cargo bookings to cancel", expiredCargos.size());

            for (CargoBooking cargo : expiredCargos) {
                try {
                    // Cancel cargo booking
                    cancelExpiredCargoBooking(cargo);
                    log.info("Recovered and cancelled expired cargo: {}", cargo.getCargoCode());
                } catch (Exception e) {
                    log.error("Error recovering cargo ID: {}", cargo.getCargoBookingId(), e);
                }
            }

            log.info("Completed startup recovery scan for cargo bookings");
        } catch (Exception e) {
            log.error("Error during startup recovery scan for cargo", e);
        }
    }

    /**
     * Periodic backup mechanism: Check for expired cargo bookings every 10 minutes
     * This catches any cargo that might have been missed
     */
    @Scheduled(fixedRate = 30 * 60 * 1000) // Run every 30 minutes
    @Transactional
    public void periodicExpiredCargosCheck() {
        log.debug("Running periodic check for expired unpaid cargo bookings...");

        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusHours(1);
            List<CargoBooking> expiredCargos = cargoBookingRepository.findExpiredPendingCargoBookings(cutoffTime);

            if (!expiredCargos.isEmpty()) {
                log.info("Periodic check found {} expired cargo bookings", expiredCargos.size());

                for (CargoBooking cargo : expiredCargos) {
                    try {
                        cancelExpiredCargoBooking(cargo);
                        log.info("Periodic check cancelled cargo: {}", cargo.getCargoCode());
                    } catch (Exception e) {
                        log.error("Error in periodic check for cargo ID: {}", cargo.getCargoBookingId(), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error during periodic expired cargo check", e);
        }
    }

    /**
     * Helper method to cancel expired cargo booking
     */
    @Transactional
    public void cancelExpiredCargoBooking(CargoBooking cargo) {
        // Validate cargo is actually expired and pending
        if (cargo.getPayment() != null && cargo.getPayment().getStatus() != PaymentStatus.pending) {
            // Already paid, don't cancel
            return;
        }

        // Check if cargo is still PENDING
        if (cargo.getStatus() != CargoStatus.PENDING) {
            return;
        }

        // Check if cargo is older than 1 hour
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(1);
        if (cargo.getCreatedAt().isAfter(cutoffTime)) {
            return;
        }

        // Cancel the cargo booking
        cargo.setStatus(CargoStatus.CANCELLED);
        cargo.setCancelledAt(LocalDateTime.now());
        cargo.setCancellationReason("Auto-cancelled: Payment not received within 1 hour");

        cargoBookingRepository.save(cargo);

        // Create tracking record for the cancellation
        createTrackingRecord(cargo, CargoStatus.CANCELLED,
                null,
                "Auto-cancelled: Payment not received within 1 hour",
                null);

        log.info("Auto-cancelled unpaid cargo booking: {} (created at: {})",
                cargo.getCargoCode(), cargo.getCreatedAt());
    }

    /**
     * Create tracking record for cargo status change
     */
    private CargoTracking createTrackingRecord(CargoBooking cargoBooking, CargoStatus status,
            String location, String notes, Long updatedByUserId) {

        CargoTracking tracking = CargoTracking.builder()
                .cargoBooking(cargoBooking)
                .status(status)
                .location(location)
                .notes(notes)
                .updatedBy(null) // System cancellation, no user
                .createdAt(LocalDateTime.now())
                .build();

        return cargoTrackingRepository.save(tracking);
    }
}
