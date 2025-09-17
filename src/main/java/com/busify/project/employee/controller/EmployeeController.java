package com.busify.project.employee.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.employee.dto.response.DriverResponseDTO;
import com.busify.project.employee.dto.response.EmployeeForOperatorResponse;
import com.busify.project.employee.dto.response.EmployeeResponseDTO;
import com.busify.project.employee.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Employee Management API")
public class EmployeeController {
    
    private final EmployeeService employeeService;

    @Operation(summary = "Get all drivers")
    @GetMapping("/drivers")
    public ApiResponse<List<EmployeeResponseDTO>> getAllDrivers() {
        try {
            List<EmployeeResponseDTO> drivers = employeeService.getAllDrivers();
            return ApiResponse.success("Get drivers list successfully", drivers);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error getting drivers list: " + e.getMessage());
        }
    }

    @Operation(summary = "Get driver by ID")
    @GetMapping("/drivers/{driverId}")
    public ApiResponse<DriverResponseDTO> getDriverById(@PathVariable Long driverId) {
        try {
            DriverResponseDTO driver = employeeService.getDriverById(driverId);
            return ApiResponse.success("Get driver information successfully", driver);
        } catch (RuntimeException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Error getting driver information: " + e.getMessage());
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

    @Operation(summary = "Get employees for operator")
    @GetMapping("/for-operator")
    public ApiResponse<List<EmployeeForOperatorResponse>> getEmployeesForOperator() {
        List<EmployeeForOperatorResponse> employees = employeeService.getAllDriverOfOperator();
        return ApiResponse.success("Get employees list successfully", employees);
    }
}