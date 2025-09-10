package com.busify.project.bus_operator.dto.response;

import com.busify.project.bus_operator.enums.OperatorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusOperatorProfileResponse {
    private Long id;
    private String name;
    private String hotline;
    private String address;
    private String email;
    private String description;
    private OperatorStatus status;
    private String avatarUrl;
}
