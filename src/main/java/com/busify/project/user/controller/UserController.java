package com.busify.project.user.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.user.dto.UserDTO;
import com.busify.project.user.dto.response.RegisterResponseDTO;
import com.busify.project.user.dto.request.RegisterRequestDTO;
import com.busify.project.user.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}