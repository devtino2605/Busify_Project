package com.busify.project.trip.service;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.dto.request.TripMGMTRequestDTO;
import com.busify.project.trip.dto.response.ReportTripResponseDTO;
import com.busify.project.trip.dto.response.TripDeleteResponseDTO;
import com.busify.project.trip.dto.response.TripMGMTResponseDTO;
import com.busify.project.trip.enums.TripStatus;

import java.util.List;

public interface TripMGMTService {
    TripMGMTResponseDTO addTrip(TripMGMTRequestDTO requestDTO);
    TripMGMTResponseDTO updateTrip(Long id, TripMGMTRequestDTO requestDTO);
    TripDeleteResponseDTO deleteTrip(Long id, boolean isDelete);
    ApiResponse<?> getAllTrips(String keyword, TripStatus status, int page, int size);
    ApiResponse<List<ReportTripResponseDTO>> reportTrips(Long operatorId);
}
