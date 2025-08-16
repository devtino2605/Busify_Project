package com.busify.project.complaint.mapper;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.complaint.dto.ComplaintAddDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseAddDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseGetDTO;
import com.busify.project.complaint.dto.response.ComplaintResponseDetailDTO;
import com.busify.project.complaint.entity.Complaint;
import com.busify.project.user.entity.Profile;
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
        ComplaintResponseAddDTO response = new ComplaintResponseAddDTO();
        response.setId(complaint.getComplaintsId());
        response.setTitle(complaint.getTitle());
        response.setDescription(complaint.getDescription());
        response.setStatus(complaint.getStatus());
        response.setAssignedAgentId(complaint.getAssignedAgent() != null ? complaint.getAssignedAgent().getId() : null);

        Profile customer = (Profile) complaint.getCustomer();
        response.setCustomerName(customer.getFullName());
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
        response.setId(complaint.getComplaintsId());
        response.setTitle(complaint.getTitle());
        response.setDescription(complaint.getDescription());
        response.setStatus(complaint.getStatus());
        response.setCreatedAt(complaint.getCreatedAt().toString());

        Profile customer = (Profile) complaint.getCustomer();
        response.setCustomerName(customer.getFullName());
        return response;
    }

    public static ComplaintResponseDetailDTO toDetailResponseDTO(Complaint complaint) {
        ComplaintResponseDetailDTO response = new ComplaintResponseDetailDTO();
        response.setId(complaint.getComplaintsId());
        response.setTitle(complaint.getTitle());
        response.setDescription(complaint.getDescription());
        response.setStatus(complaint.getStatus());
        response.setCreatedAt(complaint.getCreatedAt().toString());
        response.setUpdatedAt(complaint.getUpdatedAt().toString());

        // Customer information
        Profile customer = (Profile) complaint.getCustomer();
        ComplaintResponseDetailDTO.CustomerInfo customerInfo = new ComplaintResponseDetailDTO.CustomerInfo();
        customerInfo.setCustomerId(customer.getId());
        customerInfo.setCustomerName(customer.getFullName());
        customerInfo.setCustomerEmail(customer.getEmail());
        customerInfo.setCustomerPhone(customer.getPhoneNumber());
        customerInfo.setCustomerAddress(customer.getAddress());
        response.setCustomer(customerInfo);

        // Booking information
        if (complaint.getBooking() != null) {
            ComplaintResponseDetailDTO.BookingInfo bookingInfo = new ComplaintResponseDetailDTO.BookingInfo();
            bookingInfo.setBookingId(complaint.getBooking().getId());
            bookingInfo.setBookingCode(complaint.getBooking().getBookingCode());
            bookingInfo.setBookingStatus(complaint.getBooking().getStatus());
            bookingInfo.setTotalAmount(complaint.getBooking().getTotalAmount());
            bookingInfo.setSeatNumber(complaint.getBooking().getSeatNumber());
            bookingInfo.setBookingDate(complaint.getBooking().getCreatedAt());

            // Trip and route information
            if (complaint.getBooking().getTrip() != null) {
                bookingInfo.setRouteName(complaint.getBooking().getTrip().getRoute().getName());
                bookingInfo.setStartLocation(complaint.getBooking().getTrip().getRoute().getStartLocation().getName());
                bookingInfo.setEndLocation(complaint.getBooking().getTrip().getRoute().getEndLocation().getName());
                bookingInfo.setDepartureTime(complaint.getBooking().getTrip().getDepartureTime());
                bookingInfo.setArrivalTime(complaint.getBooking().getTrip().getEstimatedArrivalTime());
                bookingInfo.setOperatorName(complaint.getBooking().getTrip().getBus().getOperator().getName());
                bookingInfo.setBusLicensePlate(complaint.getBooking().getTrip().getBus().getLicensePlate());
            }
            response.setBooking(bookingInfo);
        }

        // Assigned agent information
        if (complaint.getAssignedAgent() != null) {
            ComplaintResponseDetailDTO.AgentInfo agentInfo = new ComplaintResponseDetailDTO.AgentInfo();
            agentInfo.setAgentId(complaint.getAssignedAgent().getId());
            if (complaint.getAssignedAgent() instanceof Profile agentProfile) {
                agentInfo.setAgentName(agentProfile.getFullName());
                agentInfo.setAgentEmail(agentProfile.getEmail());
            }
            response.setAssignedAgent(agentInfo);
        }

        return response;
    }
}