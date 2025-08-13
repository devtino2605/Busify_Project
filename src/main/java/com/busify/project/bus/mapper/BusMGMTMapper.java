package com.busify.project.bus.mapper;

import com.busify.project.bus.dto.request.BusMGMTRequestDTO;
import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.entity.Bus;
import org.springframework.stereotype.Component;

@Component
public class BusMGMTMapper {

    public static BusDetailResponseDTO toBusDetailResponseDTO(Bus bus) {
        if (bus == null) return null;

        BusDetailResponseDTO dto = new BusDetailResponseDTO();
        dto.setId(bus.getId());
        dto.setLicensePlate(bus.getLicensePlate());
        dto.setModel(bus.getModel());
        dto.setTotalSeats(bus.getTotalSeats());
        dto.setOperatorId(bus.getOperator().getId());
        dto.setSeatLayoutId(bus.getSeatLayout().getId());
        dto.setStatus(bus.getStatus());
        dto.setAmenities(bus.getAmenities());

        return dto;
    }
}
