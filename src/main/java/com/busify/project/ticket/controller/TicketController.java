package com.busify.project.ticket.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.ticket.dto.request.TicketRequestDTO;
import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping()
    public ApiResponse<List<TicketResponseDTO>> generateTickets(@RequestBody TicketRequestDTO requestDTO) {
        List<TicketResponseDTO> tickets = ticketService.createTicketsFromBooking(requestDTO.getBookingId());
        return ApiResponse.success("Tạo vé thành công", tickets);
    }
}
