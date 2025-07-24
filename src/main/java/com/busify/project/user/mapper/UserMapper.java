package com.busify.project.user.mapper;

import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.entity.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        String roleName = user.getRole() != null ? user.getRole().getName() : null;
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                roleName
        );
    }
}