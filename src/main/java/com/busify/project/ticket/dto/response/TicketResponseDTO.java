package com.busify.project.ticket.dto.response;

import com.busify.project.ticket.enums.TicketStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketResponseDTO {
    private TicketInfo tickets;

    @Data
    public static class TicketInfo {
        private Long ticketId;
        private String passengerName;
        private String passengerPhone;
        private BigDecimal price;
        private String seatNumber;
        private TicketStatus status;
        private String ticketCode;
        private Long bookingId;
    }
}
