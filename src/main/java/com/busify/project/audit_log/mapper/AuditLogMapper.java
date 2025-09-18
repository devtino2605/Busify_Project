package com.busify.project.audit_log.mapper;

import com.busify.project.audit_log.dto.response.AuditLogPageResponseDTO;
import com.busify.project.audit_log.dto.response.AuditLogResponseDTO;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.user.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditLogMapper {

    /**
     * Convert AuditLog entity to AuditLogResponseDTO
     *
     * @param auditLog the audit log entity
     * @return AuditLogResponseDTO
     */
    public AuditLogResponseDTO toDTO(AuditLog auditLog) {
        if (auditLog == null) {
            return null;
        }

        AuditLogResponseDTO dto = new AuditLogResponseDTO();
        dto.setId(auditLog.getId());
        dto.setAction(auditLog.getAction());
        dto.setTargetEntity(auditLog.getTargetEntity());
        dto.setTargetId(auditLog.getTargetId());
        dto.setDetails(auditLog.getDetails());
        dto.setTimestamp(auditLog.getTimestamp());

        // Map user information if available
        if (auditLog.getUser() != null) {
            dto.setUserId(auditLog.getUser().getId());
            dto.setUserEmail(auditLog.getUser().getEmail());

            // Check if User is actually a Profile to get fullName
            if (auditLog.getUser() instanceof Profile) {
                Profile profile = (Profile) auditLog.getUser();
                dto.setUserName(profile.getFullName());
            } else {
                // If it's just a User (not Profile), use email as name fallback
                dto.setUserName(auditLog.getUser().getEmail());
            }
        }

        return dto;
    }

    /**
     * Convert list of AuditLog entities to list of AuditLogResponseDTO
     *
     * @param auditLogs list of audit log entities
     * @return list of AuditLogResponseDTO
     */
    public List<AuditLogResponseDTO> toDTOList(List<AuditLog> auditLogs) {
        if (auditLogs == null) {
            return null;
        }

        return auditLogs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert Page<AuditLog> to AuditLogPageResponseDTO
     *
     * @param auditLogPage the paginated audit log entities
     * @return AuditLogPageResponseDTO with pagination information
     */
    public AuditLogPageResponseDTO toPageResponseDTO(Page<AuditLog> auditLogPage) {
        if (auditLogPage == null) {
            return null;
        }

        List<AuditLogResponseDTO> auditLogDTOs = auditLogPage.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new AuditLogPageResponseDTO(
                auditLogDTOs,
                auditLogPage.getNumber(),
                auditLogPage.getTotalPages(),
                auditLogPage.getTotalElements(),
                auditLogPage.getSize(),
                auditLogPage.hasNext(),
                auditLogPage.hasPrevious()
        );
    }
}