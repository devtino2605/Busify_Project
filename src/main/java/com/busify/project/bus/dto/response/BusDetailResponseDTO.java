package com.busify.project.bus.dto.response;

import java.util.Map;

import com.busify.project.bus.enums.BusStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusDetailResponseDTO {
    private Long id;
    private String licensePlate;
    private String modelName;
    private int totalSeats;
    private String operatorName;
    private String seatLayoutName;
    private BusStatus status;
    private Map<String, Object> amenities;
}
