package com.busify.project.trip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDeleteResponseDTO {
    private Long id;
    private Long routeId;
    private Long busId;
}
