package com.busify.project.ticket.service;

import com.busify.project.ticket.dto.request.TicketUpdateRequestDTO;
import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.dto.response.TicketDetailResponseDTO;
import com.busify.project.ticket.dto.response.TripPassengerListResponseDTO;

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
}
