package com.busify.project.employee.dto.response;

// No imports needed for basic types
import lombok.Data;

@Data
public class EmployeeResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String employeeType; // Using String instead of enum for flexibility
    private String status;
    private String driverLicenseNumber;
    private Long operatorId;
    private String operatorName;
}
