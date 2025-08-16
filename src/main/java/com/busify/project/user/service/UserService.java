package com.busify.project.user.service;

import java.util.List;

import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.dto.request.UserManagementFilterDTO;
import com.busify.project.user.dto.request.UserManagerUpdateOrCreateDTO;
import com.busify.project.user.dto.response.UserManagementDTO;
import com.busify.project.user.dto.response.UserManagementPageDTO;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO updateUserProfile(Long id, UserDTO userDTO);

    UserDTO findUserByEmail();

    UserDTO updateUserById(Long id, UserManagerUpdateOrCreateDTO userDTO);

    void deleteUserById(Long id);

    List<UserManagementDTO> getAllUsersForManagement();

    UserManagementPageDTO getUsersForManagementWithFilter(UserManagementFilterDTO filterDTO);

    UserDTO createUser(UserManagerUpdateOrCreateDTO userDTO);

    UserDTO getUserProfile();
}