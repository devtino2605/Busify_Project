package com.busify.project.bus.mapper;

import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.dto.response.BusImageDTO;
import com.busify.project.bus.dto.response.BusMGMTResponseDTO;
import com.busify.project.bus.entity.Bus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BusMGMTMapper {

    public static BusMGMTResponseDTO toBusDetailResponseDTO(Bus bus) {
        return BusMGMTResponseDTO.builder()
                .id(bus.getId())
                .licensePlate(bus.getLicensePlate())
                .modelId(bus.getModel().getId())
                .modelName(bus.getModel().getName())
                .totalSeats(bus.getTotalSeats())
                .operatorId(bus.getOperator().getId())
                .operatorName(bus.getOperator().getName())
                .seatLayoutId(bus.getSeatLayout().getId())
                .seatLayoutName(bus.getSeatLayout().getName())
                .status(bus.getStatus())
                .amenities(bus.getAmenities())
                .images(bus.getImages() == null ? List.of() :
                        bus.getImages().stream().map(img -> BusImageDTO.builder()
                                        .id(img.getId())
                                        .imageUrl(img.getImageUrl())
                                        .primary(img.isPrimary())
                                        .build())
                                .collect(Collectors.toList()))
                .build();
    }

}
