package com.busify.project.contract.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.contract.dto.response.ContractDTO;
import com.busify.project.contract.dto.request.ContractRequestDTO;
import com.busify.project.contract.dto.response.ContractReviewDTO;
import com.busify.project.contract.enums.ContractStatus;
import com.busify.project.contract.service.ContractService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@Tag(name = "Contract Management", description = "APIs for managing contracts between admin and bus operators")
public class ContractController {

    private final ContractService contractService;

    // ======================== BUS OPERATOR ENDPOINTS ========================

    @PostMapping
    @Operation(summary = "Create new contract proposal", description = "Bus operator creates new contract proposal")
    public ApiResponse<ContractDTO> createContract(@Valid @ModelAttribute ContractRequestDTO requestDTO) {
        ContractDTO createdContract = contractService.createContract(requestDTO);
        try {
            return ApiResponse.<ContractDTO>builder()
                    .code(HttpStatus.CREATED.value())
                    .message("Contract created successfully")
                    .result(createdContract)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<ContractDTO>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to create contract: " + e.getMessage())
                    .build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contract by ID", description = "Get contract details by ID")
    public ApiResponse<ContractDTO> getContractById(@PathVariable Long id) {
        ContractDTO contract = contractService.getContractById(id);
        try {
            return ApiResponse.<ContractDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Contract retrieved successfully")
                    .result(contract)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<ContractDTO>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve contract: " + e.getMessage())
                    .build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update contract", description = "Bus operator updates contract (only when status is PENDING or NEED_REVISION)")
    public ApiResponse<ContractDTO> updateContract(
            @PathVariable Long id,
            @Valid @ModelAttribute ContractRequestDTO requestDTO) {
        ContractDTO updatedContract = contractService.updateContract(id, requestDTO);
        try {
            return ApiResponse.<ContractDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Contract updated successfully")
                    .result(updatedContract)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<ContractDTO>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to update contract: " + e.getMessage())
                    .build();
        }
    }

    @GetMapping("/my-contracts")
    @Operation(summary = "Get all contracts for current user", description = "Get all contracts for the currently authenticated user")
    public ApiResponse<List<ContractDTO>> getMyContracts() {
        List<ContractDTO> contracts = contractService.getContractsByOperatorEmail();
        try {
            return ApiResponse.<List<ContractDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Contracts retrieved successfully")
                    .result(contracts)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<ContractDTO>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve contracts: " + e.getMessage())
                    .build();
        }
    }

    // ======================== ADMIN ENDPOINTS ========================

    @GetMapping("/admin/all")
    @Operation(summary = "Get all contracts with pagination", description = "Admin views all contract proposals")
    public ApiResponse<Page<ContractDTO>> getAllContracts(Pageable pageable) {
        Page<ContractDTO> contracts = contractService.getAllContracts(pageable);
        try {
            return ApiResponse.<Page<ContractDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("All contracts retrieved successfully")
                    .result(contracts)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Page<ContractDTO>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve contracts: " + e.getMessage())
                    .build();
        }
    }

    @GetMapping("/admin/status/{status}")
    @Operation(summary = "Get contracts by status", description = "Admin filters contracts by status")
    public ApiResponse<Page<ContractDTO>> getContractsByStatus(
            @PathVariable ContractStatus status,
            Pageable pageable) {
        Page<ContractDTO> contracts = contractService.getContractsByStatus(status, pageable);
        try {
            return ApiResponse.<Page<ContractDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Contracts by status retrieved successfully")
                    .result(contracts)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Page<ContractDTO>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve contracts: " + e.getMessage())
                    .build();
        }
    }

    @GetMapping("/admin/search")
    @Operation(summary = "Search contracts with filters", description = "Admin searches contracts with multiple filters")
    public ApiResponse<Page<ContractDTO>> searchContracts(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) ContractStatus status,
            @RequestParam(required = false) String operationArea,
            Pageable pageable) {
        Page<ContractDTO> contracts = contractService.searchContracts(email, status, operationArea, pageable);
        try {
            return ApiResponse.<Page<ContractDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Contracts retrieved successfully")
                    .result(contracts)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Page<ContractDTO>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to retrieve contracts: " + e.getMessage())
                    .build();
        }
    }

    @PostMapping("/admin/{id}/review")
    @Operation(summary = "Review contract", description = "Admin approves, rejects, or requests revision for contract")
    public ApiResponse<ContractDTO> reviewContract(
            @PathVariable Long id,
            @Valid @RequestBody ContractReviewDTO reviewDTO) {
        ContractDTO reviewedContract = contractService.reviewContract(id, reviewDTO);
        try {
            return ApiResponse.<ContractDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Contract reviewed successfully")
                    .result(reviewedContract)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<ContractDTO>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to review contract: " + e.getMessage())
                    .build();
        }
    }

    // ======================== STATISTICS ENDPOINTS ========================

    @GetMapping("/admin/statistics")
    @Operation(summary = "Get contract statistics", description = "Get contract counts by status")
    public ApiResponse<Map<String, Object>> getContractStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("pending", contractService.countContractsByStatus(ContractStatus.PENDING));
        statistics.put("accepted", contractService.countContractsByStatus(ContractStatus.ACCEPTED));
        statistics.put("rejected", contractService.countContractsByStatus(ContractStatus.REJECTED));
        statistics.put("needRevision", contractService.countContractsByStatus(ContractStatus.NEED_REVISION));
        statistics.put("active", contractService.countContractsByStatus(ContractStatus.ACTIVE));
        statistics.put("expired", contractService.countContractsByStatus(ContractStatus.EXPIRED));
        return ApiResponse.<Map<String, Object>>builder()
                .code(HttpStatus.OK.value())
                .message("Contract statistics retrieved successfully")
                .result(statistics)
                .build();
    }
}