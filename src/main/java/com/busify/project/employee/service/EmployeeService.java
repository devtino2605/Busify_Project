package com.busify.project.employee.service;

import com.busify.project.employee.dto.response.DriverResponseDTO;
import com.busify.project.employee.dto.response.EmployeeResponseDTO;
import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseDTO> getAllDrivers();
    DriverResponseDTO getDriverById(Long driverId);
}
