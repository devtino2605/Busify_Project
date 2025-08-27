package com.busify.project.bus.service;

import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.dto.response.BusForOperatorResponse;
import com.busify.project.bus.dto.response.BusLayoutResponseDTO;

import java.util.List;

public interface BusService {
    BusLayoutResponseDTO getBusSeatLayoutMap(Long busId);

    List<BusDetailResponseDTO> getBusesByOperatorId(Long operatorId);

    List<BusForOperatorResponse> getAllBuses();
}
