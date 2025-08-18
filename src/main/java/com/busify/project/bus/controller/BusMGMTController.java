package com.busify.project.bus.controller;

import com.busify.project.bus.dto.request.BusMGMTRequestDTO;
import com.busify.project.bus.dto.response.BusDeleteResponseDTO;
import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.enums.BusStatus;
import com.busify.project.bus.service.BusMGMTService;
import com.busify.project.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bus-management")
@RequiredArgsConstructor
public class BusMGMTController {

    private final BusMGMTService busMGMTService;

    @GetMapping
    public ApiResponse<?> getAllBuses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BusStatus status,
            @RequestParam(required = false) List<String> amenities,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return busMGMTService.getAllBuses(keyword, status, amenities, page, size);
    }

    @PostMapping
    public ApiResponse<BusDetailResponseDTO> addBus(@RequestBody BusMGMTRequestDTO requestDTO) {
        return ApiResponse.success("Thêm mới xe bus thành công", busMGMTService.addBus(requestDTO));
    }

    @PatchMapping("/{id}")
    public ApiResponse<BusDetailResponseDTO> updateBus(
            @PathVariable Long id,
            @RequestBody BusMGMTRequestDTO requestDTO) {
        return ApiResponse.success("Cập nhật xe bus thành công", busMGMTService.updateBus(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<BusDeleteResponseDTO> deleteBus(
            @PathVariable Long id,
            @RequestParam boolean isDelete) {
        String message = isDelete
                ? "Xóa xe khách thành công"
                : "Bạn đã xác nhận không xóa xe khách";

        return ApiResponse.success(message, busMGMTService.deleteBus(id, isDelete));
    }

}
