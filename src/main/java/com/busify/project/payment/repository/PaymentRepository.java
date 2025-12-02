package com.busify.project.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.busify.project.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

        // ===== OPTIMIZED: Use JOIN FETCH to avoid N+1 query problem =====

        /**
         * Find payment by transaction code with eager loading
         * Optimized to fetch both booking and cargoBooking to avoid lazy loading issues
         */
        @Query("SELECT p FROM Payment p " +
                        "LEFT JOIN FETCH p.booking " +
                        "LEFT JOIN FETCH p.cargoBooking " +
                        "WHERE p.transactionCode = :transactionCode")
        Optional<Payment> findByTransactionCode(@Param("transactionCode") String transactionCode);

        /**
         * Find payment by payment gateway ID with eager loading
         * Optimized to fetch both booking and cargoBooking to avoid lazy loading issues
         */
        @Query("SELECT p FROM Payment p " +
                        "LEFT JOIN FETCH p.booking " +
                        "LEFT JOIN FETCH p.cargoBooking " +
                        "WHERE p.paymentGatewayId = :paymentGatewayId")
        Optional<Payment> findByPaymentGatewayId(@Param("paymentGatewayId") String paymentGatewayId);

        // ===== BOOKING PAYMENT QUERIES =====

        /**
         * Find payment by booking ID with eager loading
         * Already optimized with JOIN FETCH
         */
        @Query("SELECT p FROM Payment p JOIN FETCH p.booking b WHERE b.Id = :bookingId")
        Payment findByBookingId(@Param("bookingId") Long bookingId);

        /**
         * Find completed payment by ID with booking and trip details
         * Already optimized with JOIN FETCH
         */
        @Query("SELECT p FROM Payment p WHERE p.paymentId = :paymentId AND p.status = 'COMPLETED'")
        Optional<Payment> findById(@Param("paymentId") Long id);

        // ===== CARGO PAYMENT QUERIES =====

        /**
         * Find payment by cargo booking ID with eager loading
         * OPTIMIZED: Added LEFT JOIN FETCH to avoid N+1 problem
         */
        @Query("SELECT p FROM Payment p " +
                        "LEFT JOIN FETCH p.booking " +
                        "LEFT JOIN FETCH p.cargoBooking cb " +
                        "WHERE cb.cargoBookingId = :cargoBookingId")
        Optional<Payment> findByCargoBookingId(@Param("cargoBookingId") Long cargoBookingId);

        /**
         * Check if cargo booking has payment
         * No need for JOIN FETCH as we only need count
         */
        @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
                        "FROM Payment p " +
                        "WHERE p.cargoBooking.cargoBookingId = :cargoBookingId")
        boolean existsByCargoBookingId(@Param("cargoBookingId") Long cargoBookingId);
}
