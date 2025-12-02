package com.busify.project.user.dto.response;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserManagementDTO {
    // Basic Info
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;

    // Status & Security
    private UserStatus status;
    private boolean emailVerified;
    private AuthProvider authProvider;

    // Role Info
    private String roleName;
    private Integer roleId;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional Management Info
    private String statusDisplayName;
    private String authProviderDisplayName;
    private long daysSinceCreated;
    private long daysSinceLastUpdate;
    private boolean canBeDeleted;
    private boolean canChangeRole;
}
