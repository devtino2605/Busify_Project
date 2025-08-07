package com.busify.project.user.service;

import java.util.List;

import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.dto.request.RegisterRequestDTO;
import com.busify.project.user.dto.response.RegisterResponseDTO;

public interface UserService {
    public List<UserDTO> getAllUsers();

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).stream()
                .filter(u -> u instanceof Profile)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return UserMapper.toDTO((Profile)user);
    }

    public UserDTO updateUserProfile(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        Profile profile = (Profile) user;
       profile.setFullName(userDTO.getFullName());
       profile.setEmail(userDTO.getEmail());
       profile.setPhoneNumber(userDTO.getPhoneNumber());
       profile.setAddress(userDTO.getAddress());
       userRepository.save(user);
       return UserMapper.toDTO((Profile) user);
    }
}

