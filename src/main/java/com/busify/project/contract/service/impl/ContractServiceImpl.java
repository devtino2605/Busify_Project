package com.busify.project.contract.service.impl;

import com.busify.project.common.service.CloudinaryService;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.contract.dto.response.ContractDTO;
import com.busify.project.contract.dto.request.ContractRequestDTO;
import com.busify.project.contract.dto.response.ContractReviewDTO;
import com.busify.project.contract.entity.Contract;
import com.busify.project.contract.enums.ContractStatus;
import com.busify.project.contract.exception.ContractAttachmentException;
import com.busify.project.contract.exception.ContractNotFoundException;
import com.busify.project.contract.exception.ContractReviewException;
import com.busify.project.contract.exception.ContractUpdateException;
import com.busify.project.contract.exception.ContractUserCreationException;
import com.busify.project.contract.mapper.ContractMapper;
import com.busify.project.contract.repository.ContractRepository;
import com.busify.project.contract.service.ContractService;
import com.busify.project.contract.service.ContractUserService;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final CloudinaryService cloudinaryService;
    private final JwtUtils jwtUtils;
    private final ContractUserService contractUserService;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    @Override
    public ContractDTO createContract(ContractRequestDTO requestDTO) {
        System.out.println("Creating contract with request: " + requestDTO);
        Contract contract = contractMapper.toEntity(requestDTO);
        contract.setLastModifiedBy("system"); // Có thể lấy từ SecurityContext
        Contract savedContract = contractRepository.save(contract);

        // Audit log for contract creation
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("CREATE");
            auditLog.setTargetEntity("CONTRACT");
            auditLog.setTargetId(savedContract.getId());
            auditLog.setDetails(String.format(
                    "{\"email\":\"%s\",\"vat_code\":\"%s\",\"operation_area\":\"%s\",\"status\":\"%s\",\"action\":\"create\"}",
                    savedContract.getEmail(), savedContract.getVATCode(), savedContract.getOperationArea(),
                    savedContract.getStatus()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for contract creation: " + e.getMessage());
        }

        return contractMapper.toDTO(savedContract);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDTO> getContractsByOperatorEmail() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        List<Contract> contracts = contractRepository.findByEmailOrderByCreatedDateDesc(email);
        return contracts.stream()
                .map(contractMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ContractDTO getContractById(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> ContractNotFoundException.withId(id));
        return contractMapper.toDTO(contract);
    }

    @Override
    public ContractDTO updateContract(Long id, ContractRequestDTO requestDTO) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> ContractNotFoundException.withId(id));

        // Chỉ cho phép update khi status là PENDING hoặc NEED_REVISION
        if (existingContract.getStatus() != ContractStatus.PENDING &&
                existingContract.getStatus() != ContractStatus.NEED_REVISION) {
            throw ContractUpdateException.invalidStatus(id, existingContract.getStatus().toString());
        }

        if (requestDTO.getAttachmentUrl() != null && !requestDTO.getAttachmentUrl().isEmpty()) {
            try {
                // Delete old license file if exists
                if (existingContract.getLicenseUrl() != null && !existingContract.getLicenseUrl().isEmpty()) {
                    String oldPublicId = cloudinaryService
                            .extractPublicId(existingContract.getLicenseUrl());
                    if (oldPublicId != null) {
                        cloudinaryService.deleteFile(oldPublicId);
                    }
                }
                // Upload new license file
                String newLicensePath = cloudinaryService.uploadFile(requestDTO.getAttachmentUrl(),
                        "licenses");
                existingContract.setLicenseUrl(newLicensePath);
            } catch (Exception e) {
                String filename = requestDTO.getAttachmentUrl() != null
                        ? requestDTO.getAttachmentUrl().getOriginalFilename()
                        : "unknown";
                throw ContractAttachmentException.uploadFailed(filename, e);
            }
        }

        // Update thông tin
        existingContract.setVATCode(requestDTO.getVATCode());
        existingContract.setEmail(requestDTO.getEmail());
        existingContract.setPhone(requestDTO.getPhone());
        existingContract.setAddress(requestDTO.getAddress());
        existingContract.setStartDate(requestDTO.getStartDate());
        existingContract.setEndDate(requestDTO.getEndDate());
        existingContract.setOperationArea(requestDTO.getOperationArea());

        existingContract.setStatus(ContractStatus.PENDING); // Reset về PENDING khi update
        existingContract.setLastModified(LocalDateTime.now());
        existingContract.setLastModifiedBy("system");

        Contract savedContract = contractRepository.save(existingContract);

        // Audit log for contract update
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("UPDATE");
            auditLog.setTargetEntity("CONTRACT");
            auditLog.setTargetId(savedContract.getId());
            auditLog.setDetails(String.format(
                    "{\"email\":\"%s\",\"vat_code\":\"%s\",\"operation_area\":\"%s\",\"old_status\":\"%s\",\"new_status\":\"%s\",\"action\":\"update\"}",
                    savedContract.getEmail(), savedContract.getVATCode(), savedContract.getOperationArea(),
                    "UNKNOWN", savedContract.getStatus()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for contract update: " + e.getMessage());
        }

        return contractMapper.toDTO(savedContract);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> getAllContracts(Pageable pageable) {
        Page<Contract> contracts = contractRepository.findAllByOrderByCreatedDateDesc(pageable);
        return contracts.map(contractMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> getAllContracts(int page, int limit) {
        // Convert 1-based page to 0-based page for Spring Data
        Pageable pageable = PageRequest.of(page - 1, limit);
        return getAllContracts(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> getAllContractsWithFilters(int page, int limit, ContractStatus status, String email,
            String operationArea) {
        // Convert 1-based page to 0-based page for Spring Data
        Pageable pageable = PageRequest.of(page - 1, limit);

        // Nếu không có filter nào thì trả về tất cả
        if (status == null && (email == null || email.trim().isEmpty())
                && (operationArea == null || operationArea.trim().isEmpty())) {
            return getAllContracts(pageable);
        }

        // Sử dụng method searchContracts đã có sẵn
        return searchContracts(email, status, operationArea, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> getContractsByStatus(ContractStatus status, Pageable pageable) {
        Page<Contract> contracts = contractRepository.findByStatusOrderByCreatedDateDesc(status, pageable);
        return contracts.map(contractMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> searchContracts(String email, ContractStatus status, String operationArea,
            Pageable pageable) {
        Page<Contract> contracts = contractRepository.findContractsWithFilters(email, status, operationArea, pageable);
        return contracts.map(contractMapper::toDTO);
    }

    @Override
    public ContractDTO reviewContract(Long id, ContractReviewDTO reviewDTO) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> ContractNotFoundException.withId(id));

        // Update admin note
        contract.setAdminNote(reviewDTO.getAdminNote());
        contract.setLastModified(LocalDateTime.now());
        contract.setLastModifiedBy("admin"); // Có thể lấy từ SecurityContext

        // Update status based on action
        switch (reviewDTO.getAction().toUpperCase()) {
            case "APPROVE":
                System.out.println("Approving contract " + reviewDTO.getAction());
                contract.setStatus(ContractStatus.ACCEPTED);
                contract.setApprovedDate(LocalDateTime.now());

                // Auto-create user and bus operator when contract is accepted
                try {
                    contractUserService.processAcceptedContract(contract);
                } catch (Exception e) {
                    throw ContractUserCreationException.userCreationFailed(id, contract.getEmail(), e);
                }
                break;
            case "REJECT":
                contract.setStatus(ContractStatus.REJECTED);
                break;
            case "REQUEST_REVISION":
                contract.setStatus(ContractStatus.NEED_REVISION);
                break;
            default:
                throw ContractReviewException.invalidAction(reviewDTO.getAction());
        }

        Contract savedContract = contractRepository.save(contract);

        // Audit log for contract review
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("REVIEW");
            auditLog.setTargetEntity("CONTRACT");
            auditLog.setTargetId(savedContract.getId());
            auditLog.setDetails(String.format(
                    "{\"email\":\"%s\",\"vat_code\":\"%s\",\"review_action\":\"%s\",\"new_status\":\"%s\",\"admin_note\":\"%s\",\"action\":\"review\"}",
                    savedContract.getEmail(), savedContract.getVATCode(), reviewDTO.getAction(),
                    savedContract.getStatus(), reviewDTO.getAdminNote()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for contract review: " + e.getMessage());
        }

        return contractMapper.toDTO(savedContract);
    }

    @Override
    @Transactional(readOnly = true)
    public long countContractsByStatus(ContractStatus status) {
        return contractRepository.countByStatus(status);
    }

    // Helper method to get current user from SecurityContext
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }

        String email = authentication.getName();
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}