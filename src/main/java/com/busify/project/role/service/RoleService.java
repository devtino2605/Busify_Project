package com.busify.project.role.service;

import java.util.List;

import com.busify.project.role.dto.RoleDTO;

public interface RoleService {
    List<RoleDTO> getAllRoles();

    RoleDTO getRoleById(Integer id);

    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO updateRole(Integer id, RoleDTO roleDTO);

    void deleteRole(Integer id);
}