package com.busify.project.employee.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.employee.dto.request.CreateDriverRequest;
import com.busify.project.employee.dto.request.UpdateDriverRequest;
import com.busify.project.employee.dto.response.DriverResponseDTO;
import com.busify.project.employee.dto.response.EmployeeResponseDTO;
import com.busify.project.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    
    private final EmployeeService employeeService;

    @GetMapping("/drivers")
    public ApiResponse<List<EmployeeResponseDTO>> getAllDrivers() {
        try {
            List<EmployeeResponseDTO> drivers = employeeService.getAllDrivers();
            return ApiResponse.success("Lấy danh sách tài xế thành công", drivers);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lấy danh sách tài xế: " + e.getMessage());
        }
    }

    @PostMapping("/drivers")
    public ApiResponse<DriverResponseDTO> createDriver(@Valid @RequestBody CreateDriverRequest request) {
        try {
            DriverResponseDTO newDriver = employeeService.createDriver(request);
            return ApiResponse.success("Tạo tài xế mới thành công", newDriver);
        } catch (RuntimeException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi tạo tài xế mới: " + e.getMessage());
        }
    }

    @GetMapping("/drivers/{driverId}")
    public ApiResponse<DriverResponseDTO> getDriverById(@PathVariable Long driverId) {
        try {
            DriverResponseDTO driver = employeeService.getDriverById(driverId);
            return ApiResponse.success("Lấy thông tin tài xế thành công", driver);
        } catch (RuntimeException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lấy thông tin tài xế: " + e.getMessage());
        }
    }

    @PutMapping("/drivers/{driverId}")
    public ApiResponse<DriverResponseDTO> updateDriver(
            @PathVariable Long driverId,
            @Valid @RequestBody UpdateDriverRequest request) {
        try {
            DriverResponseDTO updatedDriver = employeeService.updateDriver(driverId, request);
            return ApiResponse.success("Cập nhật thông tin tài xế thành công", updatedDriver);
        } catch (RuntimeException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi cập nhật thông tin tài xế: " + e.getMessage());
        }
    }

    @DeleteMapping("/drivers/{driverId}")
    public ApiResponse<Void> deleteDriver(@PathVariable Long driverId) {
        try {
            employeeService.deleteDriver(driverId);
            return ApiResponse.success("Xóa tài xế thành công", null);
        } catch (RuntimeException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi xóa tài xế: " + e.getMessage());
        }
    }
}
