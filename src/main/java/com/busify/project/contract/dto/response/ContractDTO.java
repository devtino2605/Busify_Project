package com.busify.project.contract.dto.response;

import com.busify.project.contract.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
    private Long id;
    private String vatCode;
    private String email;
    private String phone;
    private String address;
    private Instant startDate;
    private Instant endDate;
    private String operationArea;
    private String attachmentUrl;
    private Instant approvedDate;
    private String adminNote;
    private ContractStatus status;
    private Instant createdDate;
    private Instant updatedDate;
}