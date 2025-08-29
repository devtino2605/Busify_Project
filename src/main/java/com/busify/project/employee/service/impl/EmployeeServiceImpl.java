package com.busify.project.employee.service.impl;

import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.employee.dto.response.DriverResponseDTO;
import com.busify.project.employee.dto.response.EmployeeForOperatorResponse;
import com.busify.project.employee.dto.response.EmployeeResponseDTO;
import com.busify.project.employee.entity.Employee;
import com.busify.project.employee.exception.EmployeeNotFoundException;
import com.busify.project.employee.mapper.EmployeeMapper;
import com.busify.project.employee.repository.EmployeeRepository;
import com.busify.project.employee.service.EmployeeService;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserRepository userRepository;
    private final BusOperatorRepository busOperatorRepository;
    private final JwtUtils jwtUtil;

    @Override
    public List<EmployeeResponseDTO> getAllDrivers() {
        System.out.println("=== DEBUG GET ALL DRIVERS ===");
        List<Object[]> driverData = employeeRepository.findAllDrivers();
        System.out.println("Raw driver data size: " + driverData.size());

        if (!driverData.isEmpty()) {
            System.out.println("Sample data from first record:");
            Object[] firstRecord = driverData.get(0);
            for (int i = 0; i < firstRecord.length; i++) {
                System.out.println("Column " + i + ": " + firstRecord[i]);
            }
        }

        List<EmployeeResponseDTO> result = employeeMapper.toEmployeeResponseDTOList(driverData);
        System.out.println("Mapped result size: " + result.size());

        return result;
    }

    @Override
    public DriverResponseDTO getDriverById(Long driverId) {
        Employee driver = employeeRepository.findById(driverId)
                .orElseThrow(() -> EmployeeNotFoundException.driverNotFound(driverId));
        return mapToDriverResponseDTO(driver);
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        System.out.println("=== DEBUG GET ALL EMPLOYEES ===");
        List<Object[]> employeeData = employeeRepository.findAllEmployees();
        System.out.println("Raw employee data size: " + employeeData.size());

        if (!employeeData.isEmpty()) {
            System.out.println("Sample data from first record:");
            Object[] firstRecord = employeeData.get(0);
            for (int i = 0; i < firstRecord.length; i++) {
                System.out.println("Index " + i + ": " + firstRecord[i]);
            }
        }

        return employeeData.stream()
                .map(data -> employeeMapper.toEmployeeResponseDTO(data))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeForOperatorResponse> getAllDriverOfOperator() {
        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Lấy operatorId từ user
        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        return employeeRepository.findDriversByOperator(operatorId);
    }

    private DriverResponseDTO mapToDriverResponseDTO(Employee driver) {
        return DriverResponseDTO.builder()
                .id(driver.getId())
                .fullName(driver.getFullName())
                .phoneNumber(driver.getPhoneNumber())
                .address(driver.getAddress())
                .email(driver.getEmail())
                .driverLicenseNumber(driver.getDriverLicenseNumber())
                .status(driver.getStatus())
                .operatorName(driver.getOperator() != null ? driver.getOperator().getName() : null)
                .operatorId(driver.getOperator() != null ? driver.getOperator().getId() : null)
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();
    }
}
