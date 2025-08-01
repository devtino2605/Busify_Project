package com.busify.project.user.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.busify.project.user.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "profiles")
@NamedEntityGraph(name = "Profile.WithRole", attributeNodes = {
                @NamedAttributeNode("role")
})
@NamedEntityGraph(name = "Profile.WithStatus", attributeNodes = {
                @NamedAttributeNode("status"),
})
public class Profile extends User {
        @Column(name = "address")
        private String address;

        @Column(name = "full_name", nullable = false)
        private String fullName;

        @Column(name = "phone_number")
        private String phoneNumber;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        private UserStatus status = UserStatus.active;

        @CreationTimestamp
        @Column(name = "created_at", nullable = false)
        private Instant createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at", nullable = false)
        private Instant updatedAt;
}
