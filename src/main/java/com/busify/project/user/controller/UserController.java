package com.busify.project.user.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.dto.request.UserManagementFilterDTO;
import com.busify.project.user.dto.request.UserManagerUpdateOrCreateDTO;
import com.busify.project.user.dto.response.UserManagementDTO;
import com.busify.project.user.dto.response.UserManagementPageDTO;
import com.busify.project.user.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ApiResponse<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getAllUsers();
        if (users == null) {
            return ApiResponse.badRequest("User không tồn tại");
        }
        return ApiResponse.success("Lấy danh sách người dùng thành công", users);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        if (user == null) {
            return ApiResponse.badRequest("Người dùng không tồn tại");
        }
        return ApiResponse.success("Lấy thông tin người dùng thành công", user);
    }

    @PatchMapping("/{id}")
    public ApiResponse<UserDTO> updateUserProfile(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserProfile(id, userDTO);
        if (updatedUser == null) {
            return ApiResponse.badRequest("Cập nhật người dùng không thành công");
        }
        return ApiResponse.success("Cập nhật người dùng thành công", updatedUser);
    }

    @GetMapping("/profile")
    public ApiResponse<UserDTO> getUserByEmail() {
        UserDTO user = userService.findUserByEmail();
        if (user == null) {
            return ApiResponse.badRequest("Người dùng không tồn tại");
        }
        return ApiResponse.success("Lấy thông tin người dùng thành công", user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDTO> updateUserById(@PathVariable Long id,
            @RequestBody UserManagerUpdateOrCreateDTO userDTO) {
        try {
            UserDTO updatedUser = userService.updateUserById(id, userDTO);
            return ApiResponse.success("Cập nhật người dùng thành công", updatedUser);
        } catch (RuntimeException e) {
            return ApiResponse.badRequest("Cập nhật người dùng không thành công: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ApiResponse.success("Xóa người dùng thành công", "User với ID " + id + " đã được xóa (soft delete)");
        } catch (RuntimeException e) {
            return ApiResponse.badRequest("Xóa người dùng không thành công: " + e.getMessage());
        }
    }

    @GetMapping("/management")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserManagementDTO>> getUsersForManagement() {
        try {
            List<UserManagementDTO> users = userService.getAllUsersForManagement();
            return ApiResponse.success("Lấy danh sách người dùng cho quản lý thành công", users);
        } catch (RuntimeException e) {
            return ApiResponse.badRequest("Lấy danh sách người dùng không thành công: " + e.getMessage());
        }
    }

    @GetMapping("/management/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserManagementPageDTO> getUsersForManagementWithFilter(
            UserManagementFilterDTO filterDTO) {
        try {
            UserManagementPageDTO result = userService.getUsersForManagementWithFilter(filterDTO);
            return ApiResponse.success("Lấy danh sách người dùng với filter thành công", result);
        } catch (RuntimeException e) {
            return ApiResponse.badRequest("Lấy danh sách người dùng không thành công: " + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDTO> createUser(@RequestBody UserManagerUpdateOrCreateDTO userDTO) {
        try {
            UserDTO createdUser = userService.createUser(userDTO);
            return ApiResponse.success("Tạo người dùng thành công", createdUser);
        } catch (Exception e) {
            return ApiResponse.badRequest("Tạo người dùng không thành công: " + e.getMessage());
        }
    }
}
