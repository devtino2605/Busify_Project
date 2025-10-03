package com.busify.project.role.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.role.dto.RoleDTO;
import com.busify.project.role.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Role Management API")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Get all roles")
    @GetMapping
    public ApiResponse<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ApiResponse.success(roles);
    }
}