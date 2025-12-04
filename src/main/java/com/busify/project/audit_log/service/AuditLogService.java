package com.busify.project.audit_log.service;

import com.busify.project.audit_log.dto.response.AuditLogPageResponseDTO;
import com.busify.project.audit_log.entity.AuditLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AuditLogService {
    AuditLog save(AuditLog auditLog);

    Optional<AuditLog> findById(Long id);

    List<AuditLog> findAll();

    AuditLog update(Long id, AuditLog auditLog);

    void deleteById(Long id);

    // New pagination methods
    AuditLogPageResponseDTO getAllAuditLogs(int page, int size);

    AuditLogPageResponseDTO getAuditLogsByUserId(Long userId, int page, int size);

    AuditLogPageResponseDTO getAuditLogsByAction(String action, int page, int size);

    AuditLogPageResponseDTO getAuditLogsByTargetEntity(String targetEntity, int page, int size);

    AuditLogPageResponseDTO getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    AuditLogPageResponseDTO getAuditLogsByFilters(Long userId, String action, String targetEntity,
            LocalDateTime startDate, LocalDateTime endDate,
            int page, int size);
}