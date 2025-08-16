package com.busify.project.employee.dto.request;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.user.enums.UserStatus;
import lombok.Data;

@Data
public class EmployeeMGMTRequestDTO {
    private String driverLicenseNumber;
    private BusOperator operatorId;
    private String address;
    private String fullName;
    private String phoneNumber;
    private UserStatus status;
}
