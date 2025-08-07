package com.busify.project.user.service;

import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.mapper.UserMapper;
import com.busify.project.user.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAllWithRoles();
        return users
                .stream()
                .filter(user -> user instanceof Profile)
                .map(user -> UserMapper.toDTO((Profile) user))
                .collect(Collectors.toList());
    }

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