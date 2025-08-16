package com.busify.project.employee.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.employee.dto.request.EmployeeMGMTAddRequestDTO;
import com.busify.project.employee.dto.request.EmployeeMGMTRequestDTO;
import com.busify.project.employee.dto.response.EmployeeDeleteResponseDTO;
import com.busify.project.employee.dto.response.EmployeeMGMTResponseDTO;
import com.busify.project.employee.entity.Employee;
import com.busify.project.employee.service.EmployeeMGMTService;
import com.busify.project.user.enums.UserStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee-management")
@RequiredArgsConstructor
public class EmployeeMGMTController {

    private final EmployeeMGMTService employeeMGMTService;

    @GetMapping
    public ApiResponse<?> getAllEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return employeeMGMTService.getAllEmployees(keyword, status, page, size);
    }

    @PatchMapping("/{id}")
    public ApiResponse<?> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeMGMTRequestDTO requestDTO
    ) {
        return employeeMGMTService.updateEmployee(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<EmployeeDeleteResponseDTO> deleteEmployee(
            @PathVariable Long id,
            @RequestParam boolean isDelete
    ) {
        String message = isDelete
                ? "Xóa nhân viên thành công"
                : "Bạn đã xác nhận không xóa nhân viên";

        return ApiResponse.success(message, employeeMGMTService.deleteEmployee(id, isDelete));
    }

    @PostMapping
    public ApiResponse<EmployeeMGMTResponseDTO> createEmployee(
            @Valid @RequestBody EmployeeMGMTAddRequestDTO requestDTO) {
        EmployeeMGMTResponseDTO response = employeeMGMTService.createEmployee(requestDTO);
        return ApiResponse.success("Thêm nhân viên thành công", response);
    }

}
