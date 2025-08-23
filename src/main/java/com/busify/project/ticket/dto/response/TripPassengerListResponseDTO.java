package com.busify.project.ticket.dto.response;

import com.busify.project.ticket.enums.TicketStatus;
import lombok.Data;

import java.util.List;

@Data
public class TripPassengerListResponseDTO {
    private Long tripId;
    private String operatorName;
    private String routeName;
    private String departureTime;
    private List<PassengerInfo> passengers;

    @Data
    public static class PassengerInfo {
        private String passengerName;
        private String passengerPhone;
        private String email;
        private String seatNumber;
        private TicketStatus status;
        private String ticketCode;
        private Long ticketId;
    }
}
