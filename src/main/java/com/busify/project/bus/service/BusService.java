package com.busify.project.bus.service;

import java.util.List;

import com.busify.project.bus.dto.response.BusForOperatorResponse;
import org.springframework.stereotype.Service;

import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.dto.response.BusLayoutResponseDTO;

@Service
public interface BusService {
    BusLayoutResponseDTO getBusSeatLayoutMap(Long busId);

    List<BusDetailResponseDTO> getBusesByOperatorId(Long operatorId);

    List<BusForOperatorResponse> getAllBuses();
}