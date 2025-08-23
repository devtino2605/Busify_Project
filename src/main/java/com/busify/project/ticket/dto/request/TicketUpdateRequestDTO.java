package com.busify.project.ticket.dto.request;

import com.busify.project.ticket.enums.TicketStatus;
import lombok.Data;

@Data
public class TicketUpdateRequestDTO {
    private String passengerName;
    private String passengerPhone;
    private String email;
    private String seatNumber;
    private TicketStatus status;
}
