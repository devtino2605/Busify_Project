package com.busify.project.complaint.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busify.project.complaint.entity.Complaint;
import com.busify.project.complaint.enums.ComplaintStatus;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByCustomerId(Long customerId);
} 
