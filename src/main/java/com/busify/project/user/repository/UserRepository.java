package com.busify.project.user.repository;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.enums.UserStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);

    @Query("SELECT u FROM User u JOIN FETCH u.role")
    List<User> findAllWithRoles();

    @Query("SELECT p FROM Profile p JOIN FETCH p.role " +
            "WHERE (:search IS NULL OR " +
            "LOWER(p.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:authProvider IS NULL OR p.authProvider = :authProvider) " +
            "AND (:roleName IS NULL OR p.role.name = :roleName) " +
            "AND (:emailVerified IS NULL OR p.emailVerified = :emailVerified) " +
            "AND (:createdFrom IS NULL OR p.createdAt >= :createdFrom) " +
            "AND (:createdTo IS NULL OR p.createdAt <= :createdTo)")
    Page<Profile> findUsersForManagement(
            @Param("search") String search,
            @Param("status") UserStatus status,
            @Param("authProvider") AuthProvider authProvider,
            @Param("roleName") String roleName,
            @Param("emailVerified") Boolean emailVerified,
            @Param("createdFrom") Instant createdFrom,
            @Param("createdTo") Instant createdTo,
            Pageable pageable);

    // Count queries for filter summary
    @Query("SELECT COUNT(p) FROM Profile p WHERE p.status = :status")
    long countByStatus(@Param("status") UserStatus status);

    @Query("SELECT COUNT(p) FROM Profile p WHERE p.emailVerified = true")
    long countByEmailVerified();

    @Query("SELECT COUNT(p) FROM Profile p WHERE p.authProvider = :provider")
    long countByAuthProvider(@Param("provider") AuthProvider provider);
}