package com.busify.project.audit_log.service;

import com.busify.project.audit_log.entity.AuditLog;
import java.util.List;
import java.util.Optional;

public interface AuditLogService {
    AuditLog save(AuditLog auditLog);

    Optional<AuditLog> findById(Long id);

    List<AuditLog> findAll();

    AuditLog update(Long id, AuditLog auditLog);

    void deleteById(Long id);
}
