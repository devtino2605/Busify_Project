package com.busify.project.employee.service.impl;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.employee.dto.response.DriverResponseDTO;
import com.busify.project.employee.dto.response.EmployeeResponseDTO;
import com.busify.project.employee.entity.Employee;
import com.busify.project.employee.mapper.EmployeeMapper;
import com.busify.project.employee.repository.EmployeeRepository;
import com.busify.project.employee.service.EmployeeService;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;


    @Override
    public List<EmployeeResponseDTO> getAllDrivers() {
        List<Object[]> driverData = employeeRepository.findAllDrivers();
        return employeeMapper.toEmployeeResponseDTOList(driverData);
    }


    @Override
    public DriverResponseDTO getDriverById(Long driverId) {
        Employee driver = employeeRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài xế với ID: " + driverId));
        return mapToDriverResponseDTO(driver);
    }

    

    private DriverResponseDTO mapToDriverResponseDTO(Employee driver) {
        return DriverResponseDTO.builder()
                .id(driver.getId())
                .fullName(driver.getFullName())
                .phoneNumber(driver.getPhoneNumber())
                .address(driver.getAddress())
                .email(driver.getEmail())
                .driverLicenseNumber(driver.getDriverLicenseNumber())
                .employeeType(driver.getEmployeeType())
                .status(driver.getStatus())
                .operatorName(driver.getOperator() != null ? driver.getOperator().getName() : null)
                .operatorId(driver.getOperator() != null ? driver.getOperator().getId() : null)
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();
    }

}
