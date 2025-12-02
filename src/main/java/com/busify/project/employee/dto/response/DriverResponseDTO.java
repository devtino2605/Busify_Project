package com.busify.project.employee.dto.response;

import com.busify.project.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponseDTO {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String email;
    private String driverLicenseNumber;
    private UserStatus status;
    private String operatorName;
    private Long operatorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
