package com.busify.project.user.service;

import java.util.List;
import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.dto.request.RegisterRequestDTO;
import com.busify.project.user.dto.response.RegisterResponseDTO;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO updateUserProfile(Long id, UserDTO userDTO);
}