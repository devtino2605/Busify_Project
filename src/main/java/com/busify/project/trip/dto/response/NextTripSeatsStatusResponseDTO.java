package com.busify.project.trip.dto.response;

import java.util.List;

import com.busify.project.bus.dto.response.BusLayoutResponseDTO;
import com.busify.project.ticket.dto.response.TicketSeatStatusReponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NextTripSeatsStatusResponseDTO {
    private Long tripId;
    private int busSeatsCount;
    private int checkedSeatsCount;
    private int bookedSeatsCount;
    private BusLayoutResponseDTO busLayout;
    List<TicketSeatStatusReponse> seatStatuses;
}
