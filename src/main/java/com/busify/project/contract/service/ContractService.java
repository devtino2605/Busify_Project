package com.busify.project.contract.service;

import com.busify.project.contract.dto.response.ContractDTO;
import com.busify.project.contract.dto.request.ContractRequestDTO;
import com.busify.project.contract.dto.response.ContractReviewDTO;
import com.busify.project.contract.enums.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContractService {

    // Bus Operator functions
    ContractDTO createContract(ContractRequestDTO requestDTO);

    List<ContractDTO> getContractsByOperatorEmail();

    ContractDTO getContractById(Long id);

    ContractDTO updateContract(Long id, ContractRequestDTO requestDTO);

    // Admin functions
    Page<ContractDTO> getAllContracts(Pageable pageable);

    Page<ContractDTO> getAllContracts(int page, int limit);

    Page<ContractDTO> getAllContractsWithFilters(int page, int limit, ContractStatus status, String email,
            String operationArea);

    Page<ContractDTO> getContractsByStatus(ContractStatus status, Pageable pageable);

    Page<ContractDTO> searchContracts(String email, ContractStatus status, String operationArea, Pageable pageable);

    ContractDTO reviewContract(Long id, ContractReviewDTO reviewDTO);

    // Common functions
    long countContractsByStatus(ContractStatus status);
}