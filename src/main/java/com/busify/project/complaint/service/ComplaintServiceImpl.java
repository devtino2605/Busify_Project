package com.busify.project.complaint.service;

import org.springframework.stereotype.Service;

import com.busify.project.booking.repository.BookingsRepository;
import com.busify.project.complaint.dto.ComplaintAddDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseAddDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDTO;
import com.busify.project.complaint.entity.Complaint;
import com.busify.project.complaint.repository.ComplaintRepository;
import com.busify.project.user.repository.UserRepository;

@Service
public class ComplaintServiceImpl extends ComplaintService {

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository,
            BookingsRepository bookingsRepository) {
        super(complaintRepository, userRepository, bookingsRepository);
    }

    public ComplaintResponseDTO addComplaint(ComplaintAddDTO complaintAddDTO) {
        // Convert DTO to entity, save it, and return a response DTO
        Complaint complaint = toEntity(complaintAddDTO);
        complaintRepository.save(complaint);
        return toResponseDTO(complaint);
    }

    public ComplaintResponseDTO getComplaintById(Long id) {
        // Fetch the complaint by ID and convert it to a response DTO
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        return toResponseDTO(complaint);
    }

    public ComplaintResponseDTO toResponseDTO(Complaint complaint) {
        ComplaintResponseAddDTO response = new ComplaintResponseAddDTO();
        response.setStatus(complaint.getStatus());
        response.setTitle(complaint.getTitle());
        response.setAssignedAgentId(complaint.getAssignedAgent().getId());
        return response;
    }

}
