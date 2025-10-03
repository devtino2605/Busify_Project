package com.busify.project.employee.dto.response;

import com.busify.project.employee.enums.EmployeeType;
import com.busify.project.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeMGMTResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private Long operatorId;
    private String operatorName;
    private String driverLicenseNumber;
    private UserStatus status;
    private String address;
    private String phoneNumber;
    private EmployeeType employeeType;
}
