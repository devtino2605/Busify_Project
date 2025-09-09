package com.busify.project.role.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.busify.project.role.dto.RoleDTO;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.role.service.RoleService;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

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
        Optional<Role> role = roleRepository.findById(id.longValue());
        return role.map(r -> RoleDTO.builder()
                .id(r.getId())
                .name(r.getName())
                .build()).orElse(null);
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        Role savedRole = roleRepository.save(role);

        // Audit log for role creation
        try {
            User currentUser = getCurrentUser();
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("CREATE");
            auditLog.setTargetEntity("ROLE");
            auditLog.setTargetId(savedRole.getId().longValue());
            auditLog.setDetails(String.format("{\"role_id\":%d,\"role_name\":\"%s\",\"action\":\"create\"}", 
                    savedRole.getId(), savedRole.getName()));
            auditLog.setUser(currentUser);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            System.err.println("Failed to create audit log for role creation: " + e.getMessage());
        }

        return RoleDTO.builder()
                .id(savedRole.getId())
                .name(savedRole.getName())
                .build();
    }

    @Override
    public RoleDTO updateRole(Integer id, RoleDTO roleDTO) {
        Optional<Role> existingRole = roleRepository.findById(id.longValue());
        if (existingRole.isPresent()) {
            Role role = existingRole.get();
            String oldName = role.getName();
            
            role.setName(roleDTO.getName());
            Role updatedRole = roleRepository.save(role);

            // Audit log for role update
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("UPDATE");
                auditLog.setTargetEntity("ROLE");
                auditLog.setTargetId(updatedRole.getId().longValue());
                auditLog.setDetails(String.format("{\"role_id\":%d,\"old_name\":\"%s\",\"new_name\":\"%s\",\"action\":\"update\"}", 
                        updatedRole.getId(), oldName, updatedRole.getName()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
            } catch (Exception e) {
                System.err.println("Failed to create audit log for role update: " + e.getMessage());
            }

            return RoleDTO.builder()
                    .id(updatedRole.getId())
                    .name(updatedRole.getName())
                    .build();
        }
        return null;
    }

    @Override
    public void deleteRole(Integer id) {
        // Get role details before deletion for audit log
        Optional<Role> roleOpt = roleRepository.findById(id.longValue());
        
        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            
            // Audit log for role deletion (before deletion)
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("DELETE");
                auditLog.setTargetEntity("ROLE");
                auditLog.setTargetId(role.getId().longValue());
                auditLog.setDetails(String.format("{\"role_id\":%d,\"role_name\":\"%s\",\"action\":\"hard_delete\"}", 
                        role.getId(), role.getName()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
            } catch (Exception e) {
                System.err.println("Failed to create audit log for role deletion: " + e.getMessage());
            }
            
            roleRepository.deleteById(id.longValue());
        }
    }

    // Helper method to get current user from SecurityContext
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }
        
        String email = authentication.getName();
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

}