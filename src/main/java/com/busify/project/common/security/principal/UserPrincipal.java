package com.busify.project.common.security.principal;

import com.busify.project.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Simple UserPrincipal - chỉ dùng roles, không cần permission phức tạp
 */
@Getter
public class UserPrincipal implements UserDetails {
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(User user) {
        this.user = user;
        this.authorities = initializeAuthorities(user);
    }

    private Collection<? extends GrantedAuthority> initializeAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        if (user.getRole() != null) {
            // Chỉ thêm role - đơn giản và hiệu quả
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
        }
        
        return authorities.isEmpty() ? Collections.emptyList() : authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
