package com.busify.project.role.service;

import java.util.List;

import com.busify.project.role.dto.RoleDTO;
import com.busify.project.role.entity.Role;

/**
 * Simple Role Service Interface
 */
public interface RoleService {
    
    // Basic CRUD
    List<RoleDTO.RoleResponse> getAllRolesWithUserCount();
    Role getRoleById(Integer id);
    Role createRole(Role role);
    Role updateRole(Integer id, Role role);
    void deleteRole(Integer id);
    
    // User-Role assignment
    void assignRoleToUser(Integer userId, Integer roleId);
    
    // Find by name
    Role findByName(String name);
    boolean existsByName(String name);
}
