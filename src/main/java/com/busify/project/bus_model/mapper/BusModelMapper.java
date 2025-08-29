package com.busify.project.bus_model.mapper;

import com.busify.project.bus_model.dto.response.BusModelForOperatorResponse;
import com.busify.project.bus_model.entity.BusModel;

public class BusModelMapper {

    public static BusModelForOperatorResponse toDTO(BusModel busModel) {
        if (busModel == null) {
            return null;
        }
        return BusModelForOperatorResponse.builder()
                .modelId(busModel.getId())
                .modelName(busModel.getName())
                .build();
    }
}
