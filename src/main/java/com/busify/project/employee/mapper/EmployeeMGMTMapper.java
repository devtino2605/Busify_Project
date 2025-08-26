package com.busify.project.employee.mapper;

import com.busify.project.employee.dto.response.EmployeeMGMTResponseDTO;
import com.busify.project.employee.entity.Employee;

public class EmployeeMGMTMapper {
    public static EmployeeMGMTResponseDTO toEmployeeDetailResponseDTO(Employee employee) {
        if (employee == null) return null;

        EmployeeMGMTResponseDTO dto = new EmployeeMGMTResponseDTO();
        dto.setId(employee.getId());
        dto.setFullName(employee.getFullName());
        dto.setEmail(employee.getEmail());
        if (employee.getOperator() != null) {
            dto.setOperatorId(employee.getOperator().getId());
            dto.setOperatorName(employee.getOperator().getName());
        }
        dto.setDriverLicenseNumber(employee.getDriverLicenseNumber());
        dto.setStatus(employee.getStatus());
        dto.setAddress(employee.getAddress());
        dto.setPhoneNumber(employee.getPhoneNumber());
        return dto;
    }
}
