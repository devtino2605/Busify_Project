package com.busify.project.role.controller;

import com.busify.project.role.dto.RoleDTO;
import com.busify.project.role.entity.Role;
import com.busify.project.role.service.RoleService;
import com.busify.project.common.response.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Simple Role Controller - chỉ quản lý roles cơ bản
 */
@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Chỉ Admin mới được quản lý roles
public class RoleController {

    private final RoleService roleService;

    /**
     * Lấy tất cả roles
     * GET /api/admin/roles
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDTO.RoleResponse>>> getAllRoles() {
        List<RoleDTO.RoleResponse> roles = roleService.getAllRolesWithUserCount();
        
        return ResponseEntity.ok(ApiResponse.<List<RoleDTO.RoleResponse>>builder()
                .success(true)
                .message("Lấy danh sách roles thành công")
                .data(roles)
                .build());
    }

    /**
     * Lấy role by ID
     * GET /api/admin/roles/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Integer id) {
        Role role = roleService.getRoleById(id);
        
        return ResponseEntity.ok(ApiResponse.<Role>builder()
                .success(true)
                .message("Lấy role thành công")
                .data(role)
                .build());
    }

    /**
     * Tạo role mới
     * POST /api/admin/roles
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Role>> createRole(@Valid @RequestBody RoleDTO.RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        
        Role created = roleService.createRole(role);
        
        return ResponseEntity.ok(ApiResponse.<Role>builder()
                .success(true)
                .message("Tạo role thành công")
                .data(created)
                .build());
    }

    /**
     * Cập nhật role
     * PUT /api/admin/roles/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> updateRole(
            @PathVariable Integer id,
            @Valid @RequestBody RoleDTO.RoleRequest request) {
        
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        
        Role updated = roleService.updateRole(id, role);
        
        return ResponseEntity.ok(ApiResponse.<Role>builder()
                .success(true)
                .message("Cập nhật role thành công")
                .data(updated)
                .build());
    }

    /**
     * Xóa role
     * DELETE /api/admin/roles/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Xóa role thành công")
                .build());
    }

    /**
     * Gán role cho user
     * POST /api/admin/roles/assign
     */
    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<Void>> assignRoleToUser(
            @RequestBody RoleDTO.UserRoleUpdateRequest request) {
        
        roleService.assignRoleToUser(request.getUserId(), request.getRoleId());
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Gán role cho user thành công")
                .build());
    }
}
