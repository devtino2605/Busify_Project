package com.busify.project.common.principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.busify.project.permission.entity.Permission;
import com.busify.project.user.entity.User;

public class UserPrincipal {
    private Collection<? extends GrantedAuthority> initializeAuthorities(User user) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    if (user.getRole() != null) {
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
        if (user.getRole().getPermissions() != null) {
            for (Permission perm : user.getRole().getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(perm.getName()));
            }
        }
    }
    return authorities;
}
}
