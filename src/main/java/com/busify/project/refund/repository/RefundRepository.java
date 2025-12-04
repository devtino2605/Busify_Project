package com.busify.project.refund.repository;

import com.busify.project.refund.entity.Refund;
import com.busify.project.refund.enums.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

    Optional<Refund> findByRefundTransactionCode(String refundTransactionCode);

    Optional<Refund> findByGatewayRefundId(String gatewayRefundId);

    List<Refund> findByPaymentPaymentId(Long paymentId);

    List<Refund> findByStatus(RefundStatus status);

    @Query("SELECT r FROM Refund r WHERE r.payment.booking.customer.id = :customerId ORDER BY r.requestedAt DESC")
    List<Refund> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT r FROM Refund r WHERE r.requestedAt BETWEEN :startDate AND :endDate")
    List<Refund> findByRequestedAtBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(r) FROM Refund r WHERE r.status = :status")
    Long countByStatus(@Param("status") RefundStatus status);

    @Query("SELECT r FROM Refund r " +
            "LEFT JOIN FETCH r.payment p " +
            "LEFT JOIN FETCH p.booking b " +
            "LEFT JOIN FETCH b.customer c " +
            "LEFT JOIN FETCH b.tickets t " +
            "WHERE r.refundId = :refundId")
    Optional<Refund> findByIdWithAllData(@Param("refundId") Long refundId);
}
