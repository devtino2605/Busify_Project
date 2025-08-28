package com.busify.project.location.service;

import com.busify.project.location.dto.response.LocationForOperatorResponse;
import com.busify.project.location.entity.Location;
import com.busify.project.location.mapper.LocationMapper;
import com.busify.project.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public List<LocationForOperatorResponse> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream()
                .map(LocationMapper::toDTO)
                .collect(Collectors.toList());
    }
}
