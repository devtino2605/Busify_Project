package com.busify.project.role.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTOs cho Simple Role Management
 */
public class RoleDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RoleResponse {
        private Integer id;
        private String name;
        private String description;
        private Long userCount; // Số user có role này
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RoleRequest {
        @NotBlank(message = "Tên role không được trống")
        @Size(max = 50, message = "Tên role không quá 50 ký tự")
        private String name;

        @Size(max = 255, message = "Mô tả không quá 255 ký tự")
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserRoleUpdateRequest {
        private Integer userId;
        private Integer roleId;
    }
}
