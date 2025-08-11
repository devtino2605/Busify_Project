package com.busify.project.ticket.mapper;

import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.entity.Tickets;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponseDTO toTicketResponseDTO(Tickets ticket) {
        if (ticket == null) return null;

        TicketResponseDTO dto = new TicketResponseDTO();
        TicketResponseDTO.TicketInfo info = new TicketResponseDTO.TicketInfo();

        info.setTicketId(ticket.getTicketId());
        info.setPassengerName(ticket.getPassengerName());

        info.setPassengerPhone(ticket.getPassengerPhone());

        info.setPrice(ticket.getPrice());
        info.setSeatNumber(ticket.getSeatNumber());
        info.setStatus(ticket.getStatus());
        info.setTicketCode(ticket.getTicketCode());

        if (ticket.getBooking() != null) {
            info.setBookingId(ticket.getBooking().getId());
        }

        dto.setTickets(info);
        return dto;
    }
}
