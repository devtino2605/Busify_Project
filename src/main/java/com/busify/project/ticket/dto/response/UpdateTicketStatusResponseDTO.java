package com.busify.project.ticket.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketStatusResponseDTO {
    
    private int totalTickets;
    private int successfulUpdates;
    private int failedUpdates;
    private String status; // Status đã cập nhật
    private List<TicketUpdateResult> results;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TicketUpdateResult {
        private String ticketCode;
        private boolean success;
        private String message;
        private String previousStatus;
        private String newStatus;
    }
}
