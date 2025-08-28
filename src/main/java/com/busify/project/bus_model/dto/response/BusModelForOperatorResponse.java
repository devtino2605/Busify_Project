package com.busify.project.bus_model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusModelForOperatorResponse {
    private Long modelId;
    private String modelName;
}
