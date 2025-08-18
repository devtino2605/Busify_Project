package com.busify.project.user.entity;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.role.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = true)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "email_verified")
    @Builder.Default
    private Boolean emailVerified = false;

    @PrePersist
    public void prePersist() {
        if (emailVerified == null) {
            emailVerified = false;
        }
    }

    public boolean isEmailVerified() {
        return emailVerified != null ? emailVerified : false;
    }

    @Column(name = "auth_provider", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile;

}