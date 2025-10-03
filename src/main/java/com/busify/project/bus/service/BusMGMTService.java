package com.busify.project.bus.service;

import com.busify.project.bus.dto.request.BusMGMTRequestDTO;
import com.busify.project.bus.dto.response.BusDeleteResponseDTO;
import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.dto.response.BusMGMTResponseDTO;
import com.busify.project.bus.enums.BusStatus;
import com.busify.project.common.dto.response.ApiResponse;

import java.util.List;

public interface BusMGMTService {
    BusMGMTResponseDTO  addBus(BusMGMTRequestDTO requestDTO);

    BusMGMTResponseDTO updateBus(Long id, BusMGMTRequestDTO requestDTO);

    BusDeleteResponseDTO deleteBus(Long id, boolean isDelete);

    ApiResponse<?> getAllBuses(String keyword, BusStatus status, List<String> amenities, int page, int size);

}
