package com.busify.project.ticket.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingTicketsValidationResponseDTO {
    
    private Long tripId;
    private String bookingCode;
    private Long bookingId;
    private String passengerInfo; // Thông tin hành khách chính
    private List<TicketValidationDTO> tickets;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TicketValidationDTO {
        private Long ticketId;
        private String ticketCode;
        private String seatNumber;
        private String passengerName;
        private String passengerPhone;
        private String status;
        private Boolean isUsed; // Có thể thêm field này để check vé đã sử dụng chưa
    }
}
