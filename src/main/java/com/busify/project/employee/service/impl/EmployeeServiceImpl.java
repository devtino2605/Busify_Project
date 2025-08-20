package com.busify.project.employee.service.impl;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.employee.dto.request.UpdateDriverRequest;
import com.busify.project.employee.dto.response.DriverResponseDTO;
import com.busify.project.employee.dto.response.EmployeeResponseDTO;
import com.busify.project.employee.entity.Employee;
import com.busify.project.employee.mapper.EmployeeMapper;
import com.busify.project.employee.repository.EmployeeRepository;
import com.busify.project.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final BusOperatorRepository busOperatorRepository;

    @Override
    public List<EmployeeResponseDTO> getAllDrivers() {
        List<Object[]> driverData = employeeRepository.findAllDrivers();
        return employeeMapper.toEmployeeResponseDTOList(driverData);
    }

    @Override
    public DriverResponseDTO updateDriver(Long driverId, UpdateDriverRequest request) {
        Employee driver = employeeRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài xế với ID: " + driverId));

        // Update basic info
        if (request.getFullName() != null) {
            driver.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            driver.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            driver.setAddress(request.getAddress());
        }
        if (request.getEmail() != null) {
            driver.setEmail(request.getEmail());
        }
        if (request.getDriverLicenseNumber() != null) {
            driver.setDriverLicenseNumber(request.getDriverLicenseNumber());
        }
        if (request.getEmployeeType() != null) {
            driver.setEmployeeType(request.getEmployeeType());
        }
        if (request.getStatus() != null) {
            driver.setStatus(request.getStatus());
        }

        // Update operator if provided (by ID has priority over name)
        if (request.getOperatorId() != null) {
            BusOperator operator = busOperatorRepository.findById(request.getOperatorId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà điều hành với ID: " + request.getOperatorId()));
            driver.setOperator(operator);
        } else if (request.getOperatorName() != null && !request.getOperatorName().trim().isEmpty()) {
            BusOperator operator = busOperatorRepository.findByName(request.getOperatorName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà điều hành với tên: " + request.getOperatorName()));
            driver.setOperator(operator);
        }

        Employee savedDriver = employeeRepository.save(driver);
        return mapToDriverResponseDTO(savedDriver);
    }

    @Override
    public DriverResponseDTO getDriverById(Long driverId) {
        Employee driver = employeeRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài xế với ID: " + driverId));
        return mapToDriverResponseDTO(driver);
    }

    @Override
    public void deleteDriver(Long driverId) {
        Employee driver = employeeRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài xế với ID: " + driverId));
        
        // Kiểm tra xem tài xế có đang được assign cho chuyến đi nào không
        // Có thể thêm logic kiểm tra ở đây nếu cần
        
        employeeRepository.delete(driver);
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
