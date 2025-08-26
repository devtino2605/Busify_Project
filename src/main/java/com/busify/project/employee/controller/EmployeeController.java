package com.busify.project.employee.controller;

import com.busify.project.common.dto.response.ApiResponse;

import com.busify.project.employee.dto.response.DriverResponseDTO;
import com.busify.project.employee.dto.response.EmployeeResponseDTO;
import com.busify.project.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    // Test endpoint to get all employees
    @GetMapping("/all")
    public ApiResponse<List<EmployeeResponseDTO>> getAllEmployees() {
        try {
            List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();
            return ApiResponse.success("Lấy danh sách nhân viên thành công", employees);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lấy danh sách nhân viên: " + e.getMessage());
        }
    }

}
