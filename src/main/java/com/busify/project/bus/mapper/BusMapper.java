package com.busify.project.bus.mapper;

import com.busify.project.bus.dto.response.BusForOperatorResponse;
import com.busify.project.bus.entity.Bus;

public class BusMapper {

    public static BusForOperatorResponse toDTO(Bus bus) {
        if (bus == null) {
            return null;
        }
        return BusForOperatorResponse.builder()
                .id(bus.getId())
                .licensePlate(bus.getLicensePlate())
                .build();
    }
}
