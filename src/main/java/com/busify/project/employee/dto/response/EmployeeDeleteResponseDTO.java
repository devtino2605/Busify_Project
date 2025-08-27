package com.busify.project.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDeleteResponseDTO {
    private Long id;
    private String fullName;
    private String email;
}
