package com.busify.project.employee.dto.response;

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
    private String operatorName;
    private String driverLicenseNumber;
    private UserStatus status;
    private String address;
    private String phoneNumber;
}
