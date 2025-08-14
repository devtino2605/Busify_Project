package com.busify.project.bus.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusDetailResponseDTO {
    private Long id;
    private String licensePlate;
    private String model;
    private int totalSeats;
    private Long operatorId;
    private Long seatLayoutId;
    private String status;
    private Map<String, Object> amenities;
}
