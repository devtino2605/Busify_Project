package com.busify.project.user.service.impl;

import com.busify.project.common.utils.JwtUtils;
import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.mapper.UserMapper;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.user.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAllWithRoles();
        return users
                .stream()
                .filter(user -> user instanceof Profile)
                .map(user -> UserMapper.toDTO((Profile) user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with id: " + userId);
        }
        return UserMapper.toDTO((Profile) user);
    }

    @Override
    public UserDTO updateUserProfile(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with id: " + id);
        }
        Profile profile = (Profile) user;
        profile.setFullName(userDTO.getFullName());
        profile.setEmail(userDTO.getEmail());
        profile.setPhoneNumber(userDTO.getPhoneNumber());
        profile.setAddress(userDTO.getAddress());
        userRepository.save(user);
        return UserMapper.toDTO(profile);
    }

    @Override
    public UserDTO findUserByEmail() {
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        if (!(user instanceof Profile)) {
            throw new RuntimeException("User is not a Profile with email: " + email);
        }
        return UserMapper.toDTO((Profile) user);
    }
}