package com.busify.project.trip_seat.services;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.repository.PromotionRepository;
import com.busify.project.trip_seat.enums.TripSeatStatus;
import com.busify.project.trip_seat.repository.TripSeatRepository;
import com.busify.project.user.entity.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatReleaseService {

    private final TripSeatRepository tripSeatRepository;
    private final BookingRepository bookingRepository;
    private final PromotionRepository promotionRepository;

    private final Map<Long, CompletableFuture<Void>> activeReleaseTasks = new ConcurrentHashMap<>();

    @Async("seatReleaseExecutor")
    public CompletableFuture<Void> scheduleRelease(String seatNumber, Long bookingId) {
        log.info("Scheduling seat release for seatId: {}, bookingId: {}", seatNumber, bookingId);

        CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
            try {
                // Wait 1 minutes
                Thread.sleep(TimeUnit.MINUTES.toMillis(15));

                // Check if task was cancelled
                if (Thread.currentThread().isInterrupted()) {
                    log.info("Seat release task was cancelled for seatId: {}", seatNumber);
                    return;
                }

                releaseSeatIfNotPaid(seatNumber, bookingId);

            } catch (InterruptedException e) {
                log.info("Seat release task interrupted for seatId: {}", seatNumber);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Error in seat release task for seatId: {}", seatNumber, e);
            } finally {
                // Remove from active tasks
                activeReleaseTasks.remove(bookingId);
            }
        });

        // Store task để có thể cancel sau này
        activeReleaseTasks.put(bookingId, task);

        return task;
    }

    public void cancelReleaseTask(Long bookingId) {
        CompletableFuture<Void> task = activeReleaseTasks.get(bookingId);
        if (task != null) {
            task.cancel(true);
            activeReleaseTasks.remove(bookingId);
            log.info("Cancelled seat release task for bookingId: {}", bookingId);
        }
    }

    @Transactional
    public void releaseSeatIfNotPaid(String seatNumber, Long bookingId) {
        Bookings booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null)
            return;

        if (booking.getPayment() != null && booking.getPayment().getStatus() != PaymentStatus.pending)
            return;

        tripSeatRepository.findTripSeatBySeatNumberAndTripId(seatNumber, booking.getTrip().getId())
                .ifPresent(seat -> {
                    if (seat.getStatus() == TripSeatStatus.locked) {
                        seat.setStatus(TripSeatStatus.available);
                        seat.setLockingUser(null);
                        seat.setLockedAt(null);
                        tripSeatRepository.save(seat);
                    }
                });

        Promotion promo = booking.getPromotion();
        if (promo != null) {
            Profile profile = (Profile) booking.getCustomer();
            if (promo.getProfiles().contains(profile)) {
                promo.getProfiles().remove(profile);
                promotionRepository.save(promo);
            }

        }

        booking.setStatus(BookingStatus.canceled_by_operator);
        bookingRepository.save(booking);
    }

}