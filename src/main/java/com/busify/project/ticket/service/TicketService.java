package com.busify.project.ticket.service;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.ticket.dto.request.TicketUpdateRequestDTO;
import com.busify.project.ticket.dto.request.UpdateTicketStatusRequestDTO;
import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.dto.response.TicketDetailResponseDTO;
import com.busify.project.ticket.dto.response.TripPassengerListResponseDTO;
import com.busify.project.ticket.dto.response.BookingTicketsValidationResponseDTO;
import com.busify.project.ticket.dto.response.TicketBySeat;
import com.busify.project.ticket.dto.response.UpdateTicketStatusResponseDTO;
import com.busify.project.ticket.enums.SellMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<TicketResponseDTO> createTicketsFromBooking(Long bookingId, SellMethod sellMethod);

    ApiResponse<?> getAllTickets(int page, int size);

    Optional<TicketResponseDTO> searchTicketsByTicketCode(String ticketCode);

    Page<TicketResponseDTO> searchTicketsByName(String name, Pageable pageable);

    Page<TicketResponseDTO> searchTicketsByPhone(String phone, Pageable pageable);

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

    // Get Ticket by operator ID
    List<TicketResponseDTO> getTicketByOperatorId(Long operatorId);
    TicketBySeat getTicketByTripIdAndSeatNumber(Long tripId, String seatNumber);
}
