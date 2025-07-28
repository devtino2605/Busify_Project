package com.busify.project.complaint.service;

import org.springframework.stereotype.Service;

import com.busify.project.booking.repository.BookingsRepository;
import com.busify.project.complaint.dto.ComplaintAddDTO;
import com.busify.project.complaint.entity.Complaint;
import com.busify.project.complaint.repository.ComplaintRepository;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public abstract class ComplaintService {

    protected final ComplaintRepository complaintRepository;
    protected final UserRepository userRepository;
    protected final BookingsRepository bookingsRepository;

    protected ComplaintAddDTO toResponseAddDTO(Complaint complaint) {
        final ComplaintAddDTO response = new ComplaintAddDTO();
        response.setCustomerId(complaint.getCustomer().getId());
        response.setDescription(complaint.getDescription());
        response.setBookingId(complaint.getBooking().getBookingId());
        response.setStatus(complaint.getStatus());
        response.setTitle(complaint.getTitle());
        response.setAssignedAgentId(complaint.getAssignedAgent().getId());
        return response;
    }

    protected Complaint toEntity(ComplaintAddDTO complaintAddDTO) {
        Complaint complaint = new Complaint();
        complaint.setDescription(complaintAddDTO.getDescription());
        complaint.setTitle(complaintAddDTO.getTitle());
        complaint.setStatus(complaintAddDTO.getStatus());
       
        return complaint;
    }
}
