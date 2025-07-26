package com.busify.project.user.dto;

import com.busify.project.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private UserStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private String roleName; // chỉ lấy tên role thay vì toàn bộ object
}