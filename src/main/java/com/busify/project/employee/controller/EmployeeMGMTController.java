package com.busify.project.employee.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.employee.dto.request.EmployeeMGMTAddRequestDTO;
import com.busify.project.employee.dto.request.EmployeeMGMTRequestDTO;
import com.busify.project.employee.dto.response.EmployeeDeleteResponseDTO;
import com.busify.project.employee.dto.response.EmployeeMGMTResponseDTO;
import com.busify.project.employee.service.EmployeeMGMTService;
import com.busify.project.user.enums.UserStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee-management")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "Employee Management API")
public class EmployeeMGMTController {

    private final EmployeeMGMTService employeeMGMTService;

    @Operation(summary = "Get all employees")
    @GetMapping
    public ApiResponse<?> getAllEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return employeeMGMTService.getAllEmployees(keyword, status, page, size);
    }

    @Operation(summary = "Update employee information")
    @PutMapping("/{id}")
    public ApiResponse<?> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeMGMTRequestDTO requestDTO) {
        return employeeMGMTService.updateEmployee(id, requestDTO);
    }

    @Operation(summary = "Add new employee")
    @PostMapping
    public ApiResponse<EmployeeMGMTResponseDTO> addEmployee(@Valid @RequestBody EmployeeMGMTAddRequestDTO requestDTO) {
        return ApiResponse.success("Employee added successfully", employeeMGMTService.createEmployee(requestDTO));
    }

    @Operation(summary = "Delete employee")
    @DeleteMapping("/{id}")
    public ApiResponse<EmployeeDeleteResponseDTO> deleteEmployee(@PathVariable Long id, @RequestParam(defaultValue = "true") boolean isDelete) {
        return ApiResponse.success("Employee deleted successfully", employeeMGMTService.deleteEmployee(id, isDelete));
    }
}