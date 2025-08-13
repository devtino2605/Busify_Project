package com.busify.project.ticket.service;

import com.busify.project.ticket.dto.response.TicketResponseDTO;

import java.util.List;

public interface TicketService {
    List<TicketResponseDTO> createTicketsFromBooking(Long bookingId);
}
