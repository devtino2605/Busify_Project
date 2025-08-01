package com.busify.project.complaint.mapper;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.complaint.dto.ComplaintAddDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseGetDTO;
import com.busify.project.complaint.entity.Complaint;
import com.busify.project.user.entity.User;

public class ComplaintDTOMapper {
    public static ComplaintAddDTO toAddDTO(Complaint complaint) {
        final ComplaintAddDTO response = new ComplaintAddDTO();
        response.setCustomerId(complaint.getCustomer().getId());
        response.setDescription(complaint.getDescription());
        response.setBookingId(complaint.getBooking().getId());
        response.setStatus(complaint.getStatus());
        response.setTitle(complaint.getTitle());
        response.setAssignedAgentId(complaint.getAssignedAgent().getId());
        return response;
    }

    public static ComplaintResponseDTO toResponseAddDTO(Complaint complaint) {
        ComplaintResponseGetDTO response = new ComplaintResponseGetDTO();
        response.setId(complaint.getComplaintsId());
        response.setTitle(complaint.getTitle());
        response.setDescription(complaint.getDescription());
        response.setCustomerName(complaint.getCustomer().getFullName());
        return response;
    }

    public static Complaint toEntity(ComplaintAddDTO complaintAddDTO, User user, Bookings bookings,
            User assignedAgent) {
        Complaint complaint = new Complaint();
        complaint.setCustomer(user);
        complaint.setBooking(bookings);
        complaint.setAssignedAgent(assignedAgent);
        complaint.setDescription(complaintAddDTO.getDescription());
        complaint.setTitle(complaintAddDTO.getTitle());
        complaint.setStatus(complaintAddDTO.getStatus());

        return complaint;
    }

    public static ComplaintResponseGetDTO toResponseDTO(Complaint complaint) {
        ComplaintResponseGetDTO response = new ComplaintResponseGetDTO();
        response.setTitle(complaint.getTitle());
        response.setDescription(complaint.getDescription());
        response.setCustomerName(complaint.getCustomer().getFullName());
        response.setId(complaint.getComplaintsId());
        response.setCreatedAt(complaint.getCreatedAt().toString());
        return response;
    }
}