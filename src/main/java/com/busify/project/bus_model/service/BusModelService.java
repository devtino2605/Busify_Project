package com.busify.project.bus_model.service;

import com.busify.project.bus_model.dto.response.BusModelForOperatorResponse;
import com.busify.project.bus_model.entity.BusModel;
import com.busify.project.bus_model.mapper.BusModelMapper;
import com.busify.project.bus_model.repository.BusModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusModelService {

    private final BusModelRepository busModelRepository;

    public List<BusModelForOperatorResponse> getAllBusModels() {
        List<BusModel> busModels = busModelRepository.findAll();
        return busModels.stream()
                .map(BusModelMapper::toDTO)
                .collect(Collectors.toList());
    }
}
