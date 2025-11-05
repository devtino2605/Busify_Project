package com.busify.project.bus_operator.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.busify.project.bus.dto.response.BusSummaryResponseDTO;
import com.busify.project.bus_operator.enums.OperatorStatus;
import com.busify.project.user.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusOperatorForManagement {
    private Long operatorId;
    private String operatorName;
    private String email;
    private String hotline;
    private String address;
    private String licensePath;
    private OperatorStatus status;
    private String description;
    private UserDTO owner;
    private List<BusSummaryResponseDTO> busesOwned;
    private LocalDateTime dateOfResignation;
}
