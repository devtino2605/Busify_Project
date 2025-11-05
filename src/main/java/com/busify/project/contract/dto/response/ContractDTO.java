package com.busify.project.contract.dto.response;

import com.busify.project.contract.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
    private Long id;
    private String vatCode;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String operationArea;
    private String attachmentUrl;
    private LocalDateTime approvedDate;
    private String adminNote;
    private ContractStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}