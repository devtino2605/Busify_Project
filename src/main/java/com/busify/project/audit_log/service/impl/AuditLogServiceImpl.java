package com.busify.project.audit_log.service.impl;

import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.repository.AuditLogRepository;
import com.busify.project.audit_log.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

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
}
