package com.busify.project.complaint.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.busify.project.complaint.entity.Complaint;
import com.busify.project.complaint.enums.ComplaintStatus;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findAllByStatus(ComplaintStatus status);

    List<Complaint> findAllByCustomerId(Long customerId);

    List<Complaint> findAllByAssignedAgentId(Long assignedAgentId);

    List<Complaint> findAllByBookingId(Long bookingId);

    List<Complaint> findAllByTitleContainingIgnoreCase(String title);

    List<Complaint> findAllByDescriptionContainingIgnoreCase(String description);

    @Query("SELECT c FROM Complaint c WHERE c.booking.trip.bus.operator.id = :busOperatorId")
    List<Complaint> findByBusOperatorComplaint(@Param("busOperatorId") Long busOperatorId);

    @Query("SELECT c FROM Complaint c WHERE c.booking.trip.id = :tripId")
    List<Complaint> findByTripId(@Param("tripId") Long tripId);
}
