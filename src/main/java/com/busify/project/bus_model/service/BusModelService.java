package com.busify.project.bus_model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.busify.project.bus_model.entity.BusModel;
import com.busify.project.bus_model.repository.BusModelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusModelService {

    private final BusModelRepository busModelRepository;

    public List<BusModel> getAllBusModels() {
        return busModelRepository.findAll();
    }
}
