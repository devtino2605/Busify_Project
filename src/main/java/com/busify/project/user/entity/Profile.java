package com.busify.project.user.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.busify.project.promotion.entity.Promotion;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.busify.project.user.enums.UserStatus;

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
        private Instant createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at", nullable = false)
        private Instant updatedAt;

        @ManyToMany(mappedBy = "profiles")
        private Set<Promotion> promotions = new HashSet<>();

}