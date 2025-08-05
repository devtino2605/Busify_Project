package com.busify.project.bus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusLayoutResponseDTO {
    private int rows;
    private int cols;
    private int floors;
}
