package com.busify.project.audit_log.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String action;
    private String targetEntity;
    private Long targetId;
    private String details;
    private LocalDateTime timestamp;
}