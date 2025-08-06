package com.busify.project.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.busify.project.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionCode(String transactionCode);

    Optional<Payment> findByPaymentGatewayId(String paymentGatewayId);

    @Query("SELECT p FROM Payment p JOIN FETCH p.booking b WHERE b.id = :bookingId")
    Payment findByBookingId(Long bookingId);

    @Query("SELECT p FROM Payment p JOIN FETCH p.booking b JOIN FETCH b.trip t WHERE p.id = :paymentId AND p.status = 'COMPLETED'")
    Optional<Payment> findById(@Param("paymentId") Long id);
}
