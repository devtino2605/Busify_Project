package com.busify.project.bus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusDeleteResponseDTO {
    private Long id;
    private String licensePlate;
    private String modelName;
    private String operatorName;
    private String seatLayoutName;
}
