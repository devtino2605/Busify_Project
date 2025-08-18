package com.busify.project.user.mapper;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.dto.response.UserManagementDTO;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.enums.UserStatus;

import java.time.Duration;
import java.time.Instant;

public class UserMapper {
    public static UserDTO toDTO(Profile user) {
        String roleName = user.getRole() != null ? user.getRole().getName() : null;
        Integer roleId = user.getRole() != null ? user.getRole().getId() : null;

        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getStatus(),
                user.isEmailVerified(),
                user.getAuthProvider(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                roleName,
                roleId);
    }

    public static UserManagementDTO toManagementDTO(Profile user) {
        String roleName = user.getRole() != null ? user.getRole().getName() : null;
        Integer roleId = user.getRole() != null ? user.getRole().getId() : null;

        // Calculate days since created and last update
        Instant now = Instant.now();
        long daysSinceCreated = user.getCreatedAt() != null ? Duration.between(user.getCreatedAt(), now).toDays() : 0;
        long daysSinceLastUpdate = user.getUpdatedAt() != null ? Duration.between(user.getUpdatedAt(), now).toDays()
                : 0;

        // Determine management permissions
        boolean canBeDeleted = !("ADMIN".equals(roleName)); // Admin cannot be deleted
        boolean canChangeRole = !("ADMIN".equals(roleName)) || user.getId() != 1; // Cannot change admin role, except
                                                                                  // not main admin

        return UserManagementDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .status(user.getStatus())
                .emailVerified(user.isEmailVerified())
                .authProvider(user.getAuthProvider())
                .roleName(roleName)
                .roleId(roleId)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .statusDisplayName(getStatusDisplayName(user.getStatus()))
                .authProviderDisplayName(getAuthProviderDisplayName(user.getAuthProvider()))
                .daysSinceCreated(daysSinceCreated)
                .daysSinceLastUpdate(daysSinceLastUpdate)
                .canBeDeleted(canBeDeleted)
                .canChangeRole(canChangeRole)
                .build();
    }

    private static String getStatusDisplayName(UserStatus status) {
        switch (status) {
            case active:
                return "Hoạt động";
            case inactive:
                return "Không hoạt động";
            case suspended:
                return "Bị khóa";
            default:
                return "Không xác định";
        }
    }

    private static String getAuthProviderDisplayName(AuthProvider provider) {
        switch (provider) {
            case LOCAL:
                return "Đăng ký thường";
            case GOOGLE:
                return "Google OAuth";
            default:
                return "Không xác định";
        }
    }
}