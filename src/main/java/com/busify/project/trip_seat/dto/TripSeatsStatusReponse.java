package com.busify.project.trip_seat.dto; // Điều chỉnh package theo dự án của bạn


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripSeatsStatusReponse {
    private Long tripId;
    private List<SeatStatus> seatsStatus;
}