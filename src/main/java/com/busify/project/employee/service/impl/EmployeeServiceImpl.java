package com.busify.project.employee.service.impl;

import com.busify.project.employee.dto.response.EmployeeResponseDTO;
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

    @Override
    public List<EmployeeResponseDTO> getAllDrivers() {
        List<Object[]> driverData = employeeRepository.findAllDrivers();
        return employeeMapper.toEmployeeResponseDTOList(driverData);
    }
}
