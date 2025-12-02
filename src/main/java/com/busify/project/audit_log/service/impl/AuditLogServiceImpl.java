package com.busify.project.audit_log.service.impl;

import com.busify.project.audit_log.dto.response.AuditLogPageResponseDTO;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.mapper.AuditLogMapper;
import com.busify.project.audit_log.repository.AuditLogRepository;
import com.busify.project.audit_log.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    public AuditLog save(AuditLog auditLog) {
        auditLog.setTimestamp(LocalDateTime.now());
        return auditLogRepository.save(auditLog);
    }

    @Override
    public Optional<AuditLog> findById(Long id) {
        return auditLogRepository.findById(id);
    }

    @Override
    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }

    @Override
    public AuditLog update(Long id, AuditLog auditLog) {
        return auditLogRepository.findById(id)
                .map(existingLog -> {
                    existingLog.setAction(auditLog.getAction());
                    existingLog.setTargetEntity(auditLog.getTargetEntity());
                    existingLog.setTargetId(auditLog.getTargetId());
                    existingLog.setDetails(auditLog.getDetails());
                    existingLog.setUser(auditLog.getUser());
                    return auditLogRepository.save(existingLog);
                })
                .orElseThrow(() -> new RuntimeException("AuditLog not found with id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        auditLogRepository.deleteById(id);
    }

    @Override
    public AuditLogPageResponseDTO getAllAuditLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogPage = auditLogRepository.findAllByOrderByTimestampDesc(pageable);
        return auditLogMapper.toPageResponseDTO(auditLogPage);
    }

    @Override
    public AuditLogPageResponseDTO getAuditLogsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogPage = auditLogRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
        return auditLogMapper.toPageResponseDTO(auditLogPage);
    }

    @Override
    public AuditLogPageResponseDTO getAuditLogsByAction(String action, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogPage = auditLogRepository.findByActionOrderByTimestampDesc(action, pageable);
        return auditLogMapper.toPageResponseDTO(auditLogPage);
    }

    @Override
    public AuditLogPageResponseDTO getAuditLogsByTargetEntity(String targetEntity, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogPage = auditLogRepository.findByTargetEntityOrderByTimestampDesc(targetEntity, pageable);
        return auditLogMapper.toPageResponseDTO(auditLogPage);
    }

    @Override
    public AuditLogPageResponseDTO getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogPage = auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(startDate, endDate,
                pageable);
        return auditLogMapper.toPageResponseDTO(auditLogPage);
    }

    @Override
    public AuditLogPageResponseDTO getAuditLogsByFilters(Long userId, String action, String targetEntity,
            LocalDateTime startDate, LocalDateTime endDate,
            int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuditLog> auditLogPage = auditLogRepository.findByFiltersOrderByTimestampDesc(
                userId, action, targetEntity, startDate, endDate, pageable);
        return auditLogMapper.toPageResponseDTO(auditLogPage);
    }
}