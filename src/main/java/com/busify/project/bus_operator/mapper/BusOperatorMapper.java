package com.busify.project.bus_operator.mapper;

import com.busify.project.bus_operator.dto.response.BusOperatorFilterTripResponse;
import com.busify.project.bus_operator.entity.BusOperator;

public class BusOperatorMapper {

    public static BusOperatorFilterTripResponse toDTO(BusOperator busOperator) {
        if (busOperator == null) return null;

        BusOperatorFilterTripResponse dto = new BusOperatorFilterTripResponse();
        dto.setId(busOperator.getId());
        dto.setName(busOperator.getName());

        return dto;
    }
}
