package com.busify.project.bus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusSummaryResponseDTO {
    private Long id;
    private Long operatorId;
    private String licensePlate;
    private String model;
    private String status;
}
