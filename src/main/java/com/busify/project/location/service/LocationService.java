package com.busify.project.location.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.location.dto.response.LocationDTO;
import com.busify.project.location.dto.response.LocationForOperatorResponse;
import com.busify.project.location.entity.Location;
import com.busify.project.location.mapper.LocationMapper;
import com.busify.project.location.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

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

    /**
     * Lấy tất cả locations để làm dropdown cho route stops
     */
    public ApiResponse<List<LocationDTO>> getAllLocationsForDropdown() {
        try {
            List<Location> locations = locationRepository.findAllOrderByCityAndName();
            List<LocationDTO> locationDTOs = locations.stream()
                    .map(this::toLocationDTO)
                    .collect(Collectors.toList());
            
            return ApiResponse.success("Lấy danh sách điểm dừng thành công", locationDTOs);
        } catch (Exception e) {
            return ApiResponse.error(500, "Có lỗi xảy ra khi lấy danh sách điểm dừng: " + e.getMessage());
        }
    }

    /**
     * Tìm kiếm locations theo keyword cho dropdown
     */
    public ApiResponse<List<LocationDTO>> searchLocationsForDropdown(String keyword) {
        try {
            List<Location> locations;
            if (keyword == null || keyword.trim().isEmpty()) {
                locations = locationRepository.findAllOrderByCityAndName();
            } else {
                locations = locationRepository.searchByNameOrCity(keyword.trim());
            }
            
            List<LocationDTO> locationDTOs = locations.stream()
                    .map(this::toLocationDTO)
                    .collect(Collectors.toList());
            
            return ApiResponse.success("Tìm kiếm điểm dừng thành công", locationDTOs);
        } catch (Exception e) {
            return ApiResponse.error(500, "Có lỗi xảy ra khi tìm kiếm điểm dừng: " + e.getMessage());
        }
    }

    /**
     * Helper method để convert Location entity sang LocationDTO
     */
    private LocationDTO toLocationDTO(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setAddress(location.getAddress());
        dto.setCity(location.getCity());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setRegion(location.getRegion());
        return dto;
    }
}
