package com.busify.project.ticket.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.ticket.dto.request.TicketRequestDTO;
import com.busify.project.ticket.dto.request.TicketUpdateRequestDTO;
import com.busify.project.ticket.dto.request.ValidateBookingTripRequestDTO;
import com.busify.project.ticket.dto.request.UpdateTicketStatusRequestDTO;
import com.busify.project.ticket.dto.response.TicketDetailResponseDTO;
import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.dto.response.TripPassengerListResponseDTO;
import com.busify.project.ticket.dto.response.BookingTicketsValidationResponseDTO;
import com.busify.project.ticket.dto.response.UpdateTicketStatusResponseDTO;
import com.busify.project.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping()
    public ApiResponse<List<TicketResponseDTO>> getAllTickets() {
        List<TicketResponseDTO> tickets = ticketService.getAllTickets();
        return ApiResponse.success("Lấy tất cả vé thành công", tickets);
    }

    @GetMapping("/{ticketCode}")
    public ApiResponse<TicketDetailResponseDTO> getTicketById(@PathVariable String ticketCode) {
        Optional<TicketDetailResponseDTO> ticket = ticketService.getTicketById(ticketCode);
        if (ticket.isPresent()) {
            return ApiResponse.success("Lấy thông tin vé thành công", ticket.get());
        } else {
            return ApiResponse.error(404, "Không tìm thấy vé với mã: " + ticketCode);
        }
    }

    @GetMapping("/search")
    public ApiResponse<List<TicketResponseDTO>> searchTickets(@RequestParam(required = false) String ticketCode,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone) {

        // check param
        if (ticketCode != null && !ticketCode.isBlank()) {
            return ApiResponse.success("search by ticket code: " + ticketCode,
                    ticketService.searchTicketsByTicketCode(ticketCode).stream().toList());
        }
        if (name != null && !name.isBlank()) {
            return ApiResponse.success("search by name: " + name, ticketService.searchTicketsByName(name));
        }
        if (phone != null && !phone.isBlank()) {
            return ApiResponse.success("search by phone: " + phone, ticketService.searchTicketsByPhone(phone));
        }
        return ApiResponse.success("no search criteria provided", List.of());
    }

    @GetMapping("/trip/{tripId}/passengers")
    public ApiResponse<TripPassengerListResponseDTO> getPassengersByTrip(@PathVariable Long tripId) {
        try {
            TripPassengerListResponseDTO passengers = ticketService.getPassengersByTripId(tripId);
            return ApiResponse.success("Lấy danh sách hành khách thành công", passengers);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Lỗi khi lấy danh sách hành khách: " + e.getMessage());
        }
    }

    @PutMapping("/trip/{tripId}/ticket/{ticketId}")
    public ApiResponse<TicketResponseDTO> updateTicketInTrip(
            @PathVariable Long tripId,
            @PathVariable Long ticketId,
            @RequestBody TicketUpdateRequestDTO updateRequest) {
        try {
            TicketResponseDTO updatedTicket = ticketService.updateTicketInTrip(tripId, ticketId, updateRequest);
            return ApiResponse.success("Cập nhật thông tin vé thành công", updatedTicket);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(404, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Lỗi khi cập nhật thông tin vé: " + e.getMessage());
        }
    }

    @DeleteMapping("/{ticketCode}")
    public ApiResponse<Boolean> deleteTicket(@PathVariable String ticketCode) {
        try {
            ticketService.deleteTicketByCode(ticketCode);
            return ApiResponse.success("Xóa vé thành công", true);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(404, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Lỗi khi xóa vé: " + e.getMessage());
        }
    }

    @PostMapping("/validate-booking-trip")
    public ApiResponse<BookingTicketsValidationResponseDTO> validateBookingTrip(
            @RequestBody ValidateBookingTripRequestDTO request) {
        try {
            BookingTicketsValidationResponseDTO response = ticketService.validateBookingTrip(
                request.getTripId(), 
                request.getBookingCode()
            );
            return ApiResponse.success("Xác thực booking và trip thành công", response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Lỗi khi xác thực booking và trip: " + e.getMessage());
        }
    }

    // Alternative endpoint with query parameters for easier testing
    @GetMapping("/validate-booking-trip")
    public ApiResponse<BookingTicketsValidationResponseDTO> validateBookingTripByParams(
            @RequestParam Long tripId,
            @RequestParam String bookingCode) {
        try {
            BookingTicketsValidationResponseDTO response = ticketService.validateBookingTrip(tripId, bookingCode);
            return ApiResponse.success("Xác thực booking và trip thành công", response);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Lỗi khi xác thực booking và trip: " + e.getMessage());
        }
    }

    @PatchMapping("/update-status")
    public ApiResponse<UpdateTicketStatusResponseDTO> updateTicketStatus(
            @RequestBody UpdateTicketStatusRequestDTO request) {
        try {
            UpdateTicketStatusResponseDTO response = ticketService.updateTicketStatus(request);
            
            if (response.getFailedUpdates() == 0) {
                return ApiResponse.success("Cập nhật trạng thái vé thành công", response);
            } else if (response.getSuccessfulUpdates() > 0) {
                return ApiResponse.success("Cập nhật một phần thành công", response);
            } else {
                return ApiResponse.<UpdateTicketStatusResponseDTO>builder()
                    .code(400)
                    .message("Không thể cập nhật bất kỳ vé nào")
                    .result(response)
                    .build();
            }
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Lỗi khi cập nhật trạng thái vé: " + e.getMessage());
        }
    }

}
