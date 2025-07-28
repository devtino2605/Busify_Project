package com.busify.project.complaint.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.busify.project.booking.repository.BookingsRepository;
import com.busify.project.complaint.dto.ComplaintAddDTO;
import com.busify.project.complaint.dto.ComplaintUpdateDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseListDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseAddDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDTO;
import com.busify.project.complaint.entity.Complaint;
import com.busify.project.complaint.enums.ComplaintStatus;
import com.busify.project.complaint.repository.ComplaintRepository;
import com.busify.project.user.repository.UserRepository;

@Service
public class ComplaintServiceImpl extends ComplaintService {

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository,
            BookingsRepository bookingsRepository) {
        super(complaintRepository, userRepository, bookingsRepository);
    }

    public ComplaintResponseDTO addComplaint(ComplaintAddDTO complaintAddDTO) {
        Complaint complaint = toEntity(complaintAddDTO);
        complaintRepository.save(complaint);
        return toResponseDTO(complaint);
    }

    public ComplaintResponseDTO getComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        return toResponseDTO(complaint);
    }

    public ComplaintResponseListDTO getAllComplaintsByBooking(Long bookingId) {
        List<Complaint> complaints = complaintRepository.findAllByBookingId(bookingId);
        List<ComplaintAddDTO> responseList = complaints.stream()
                .map(this::toResponseAddDTO)
                .collect(Collectors.toList());
        return new ComplaintResponseListDTO(responseList);
    }

    public ComplaintResponseDTO toResponseDTO(Complaint complaint) {
        ComplaintResponseAddDTO response = new ComplaintResponseAddDTO();
        response.setStatus(complaint.getStatus());
        response.setTitle(complaint.getTitle());
        response.setAssignedAgentId(complaint.getAssignedAgent().getId());
        response.setDescription(complaint.getDescription());
        return response;
    }

    public ComplaintResponseListDTO getAllComplaintsByCustomer(Long customerId) {
        List<Complaint> complaints = complaintRepository.findAllByCustomerId(customerId);
        List<ComplaintAddDTO> responseList = complaints.stream()
                .map(this::toResponseAddDTO)
                .collect(Collectors.toList());
        return new ComplaintResponseListDTO(responseList);
    }

    public ComplaintResponseDTO updateComplaint(Long id, ComplaintUpdateDTO complaintUpdateDTO) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint
                .setTitle(complaintUpdateDTO.getTitle() != null ? complaintUpdateDTO.getTitle() : complaint.getTitle());
        complaint.setDescription(complaintUpdateDTO.getDescription() != null ? complaintUpdateDTO.getDescription()
                : complaint.getDescription());
        complaint.setStatus(ComplaintStatus.New);
        complaintRepository.save(complaint);
        return toResponseDTO(complaint);
    }

    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaintRepository.delete(complaint);
    }
}
