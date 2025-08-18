package com.busify.project.contract.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractReviewDTO {
    private String adminNote;
    private String action; 
}