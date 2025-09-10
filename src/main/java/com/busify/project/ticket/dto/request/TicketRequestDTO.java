package com.busify.project.ticket.dto.request;

import com.busify.project.ticket.enums.SellMethod;

import lombok.Data;

@Data
public class TicketRequestDTO {
    private Long bookingId;
    private SellMethod sellMethod;
}
