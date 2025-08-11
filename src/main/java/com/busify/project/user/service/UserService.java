package com.busify.project.user.service;

import java.util.List;
import com.busify.project.user.dto.UserDTO;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO updateUserProfile(Long id, UserDTO userDTO);

    UserDTO getUserProfile();
}