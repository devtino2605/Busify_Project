package com.busify.project.employee.service;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.employee.dto.request.EmployeeMGMTAddRequestDTO;
import com.busify.project.employee.dto.request.EmployeeMGMTRequestDTO;
import com.busify.project.employee.dto.response.EmployeeDeleteResponseDTO;
import com.busify.project.employee.dto.response.EmployeeMGMTResponseDTO;
import com.busify.project.employee.entity.Employee;
import com.busify.project.user.enums.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface EmployeeMGMTService {
    ApiResponse<?> getAllEmployees(String keyword, UserStatus status, int page, int size);
    ApiResponse<?> updateEmployee(Long id, EmployeeMGMTRequestDTO requestDTO);
    EmployeeDeleteResponseDTO deleteEmployee(Long id, boolean isDelete);
    EmployeeMGMTResponseDTO createEmployee(EmployeeMGMTAddRequestDTO dto);
}
