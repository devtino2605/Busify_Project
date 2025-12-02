package com.busify.project.user.dto;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private UserStatus status;
    private boolean emailVerified;
    private AuthProvider authProvider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String roleName;
    private Integer roleId;
}