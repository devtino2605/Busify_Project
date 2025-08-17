package com.busify.project.ticket.service;

import com.busify.project.ticket.dto.response.TicketResponseDTO;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<TicketResponseDTO> createTicketsFromBooking(Long bookingId);
    List<TicketResponseDTO> getAllTickets();
    Optional<TicketResponseDTO> searchTicketsByTicketCode(String ticketCode);
    List<TicketResponseDTO> searchTicketsByName(String name);
    List<TicketResponseDTO> searchTicketsByPhone(String phone);
}
