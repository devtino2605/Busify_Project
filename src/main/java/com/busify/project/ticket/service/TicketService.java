package com.busify.project.ticket.service;

import com.busify.project.ticket.dto.request.TicketUpdateRequestDTO;
import com.busify.project.ticket.dto.request.UpdateTicketStatusRequestDTO;
import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.dto.response.TicketDetailResponseDTO;
import com.busify.project.ticket.dto.response.TripPassengerListResponseDTO;
import com.busify.project.ticket.dto.response.BookingTicketsValidationResponseDTO;
import com.busify.project.ticket.dto.response.UpdateTicketStatusResponseDTO;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<TicketResponseDTO> createTicketsFromBooking(Long bookingId);

    List<TicketResponseDTO> getAllTickets();

    Optional<TicketResponseDTO> searchTicketsByTicketCode(String ticketCode);

    List<TicketResponseDTO> searchTicketsByName(String name);

    List<TicketResponseDTO> searchTicketsByPhone(String phone);

    // New method for getting ticket details by ID
    Optional<TicketDetailResponseDTO> getTicketById(String ticketCode);

    // New method for getting passengers list by trip ID
    TripPassengerListResponseDTO getPassengersByTripId(Long tripId);

    // New method for updating ticket in a specific trip
    TicketResponseDTO updateTicketInTrip(Long tripId, Long ticketId, TicketUpdateRequestDTO updateRequest);

    // New method for deleting ticket by code
    void deleteTicketByCode(String ticketCode);

    // New method for validating booking and trip relationship
    BookingTicketsValidationResponseDTO validateBookingTrip(Long tripId, String bookingCode);

    // New method for updating ticket status (used/cancelled)
    UpdateTicketStatusResponseDTO updateTicketStatus(UpdateTicketStatusRequestDTO request);

    // Tự động hủy các vé có status = valid khi trip chuyển sang departed
    int autoCancelValidTicketsWhenTripDeparted(Long tripId);
}
