package com.busify.project.role.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.busify.project.role.dto.RoleDTO;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.role.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> roles = roleRepository.findAll()
                .stream()
                .map(role -> RoleDTO.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .collect(Collectors.toList());
        return roles;
    }

    @Override
    public RoleDTO getRoleById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoleById'");
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createRole'");
    }

    @Override
    public RoleDTO updateRole(Integer id, RoleDTO roleDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateRole'");
    }

    @Override
    public void deleteRole(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRole'");
    }

}