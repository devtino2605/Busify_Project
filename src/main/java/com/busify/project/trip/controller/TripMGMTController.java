package com.busify.project.trip.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.dto.request.TripMGMTRequestDTO;
import com.busify.project.trip.dto.response.TripDeleteResponseDTO;
import com.busify.project.trip.dto.response.TripMGMTResponseDTO;
import com.busify.project.trip.enums.TripStatus;
import com.busify.project.trip.service.TripMGMTService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip-management")
@RequiredArgsConstructor
public class TripMGMTController {

    private final TripMGMTService tripMGMTService;

    @GetMapping
    public ApiResponse<?> getAllTrips(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TripStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return tripMGMTService.getAllTrips(keyword, status, page, size);
    }

    @PostMapping
    public ApiResponse<TripMGMTResponseDTO> addTrip(@Valid @RequestBody TripMGMTRequestDTO requestDTO) {
        return ApiResponse.success("Thêm chuyến đi thành công", tripMGMTService.addTrip(requestDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<TripMGMTResponseDTO> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripMGMTRequestDTO requestDTO
    ) {
        return ApiResponse.success("Cập nhật chuyến đi thành công", tripMGMTService.updateTrip(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<TripDeleteResponseDTO> deleteTrip(
            @PathVariable Long id,
            @RequestParam boolean isDelete
    ) {
        String message = isDelete
                ? "Xóa chuyến đi thành công"
                : "Bạn đã xác nhận không xóa chuyến đi";

        return ApiResponse.success(message, tripMGMTService.deleteTrip(id, isDelete));
    }
}
