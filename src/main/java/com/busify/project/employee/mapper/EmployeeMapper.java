package com.busify.project.employee.mapper;

import com.busify.project.employee.dto.response.EmployeeResponseDTO;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {
    
    public EmployeeResponseDTO toEmployeeResponseDTO(Object[] row) {
        if (row == null) {
            return null;
        }

        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setId(row[0] != null ? ((Number) row[0]).longValue() : null);
        response.setFullName((String) row[1]);
        response.setEmail((String) row[2]);
        response.setPhoneNumber((String) row[3]);
        response.setEmployeeType(row[4] != null ? row[4].toString() : null);
        response.setStatus(row[5] != null ? row[5].toString() : null);
        response.setDriverLicenseNumber((String) row[6]);
        response.setOperatorId(row[7] != null ? ((Number) row[7]).longValue() : null);
        response.setOperatorName((String) row[8]);
        
        return response;
    }
    
    public List<EmployeeResponseDTO> toEmployeeResponseDTOList(List<Object[]> rows) {
        if (rows == null) {
            return Collections.emptyList();
        }
        return rows.stream()
                .map(this::toEmployeeResponseDTO)
                .collect(Collectors.toList());
    }
}
