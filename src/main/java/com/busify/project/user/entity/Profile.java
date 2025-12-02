package com.busify.project.user.entity;

import java.time.LocalDateTime;

import com.busify.project.user.enums.UserStatus;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "profiles")
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
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at", nullable = false)
        private LocalDateTime updatedAt;

}