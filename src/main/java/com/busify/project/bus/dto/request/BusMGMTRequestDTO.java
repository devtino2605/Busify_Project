package com.busify.project.bus.dto.request;

import com.busify.project.bus.enums.BusStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusMGMTRequestDTO {
    private String licensePlate;
    private String model;
    private Long operatorId;
    private Integer seatLayoutId;
    private Map<String, Object> amenities;
    private BusStatus status;
}
