package com.busify.project.user.repository;

import com.busify.project.user.entity.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByRefreshToken(String refreshToken);

    @Query("SELECT u FROM User u JOIN FETCH u.role")
    List<User> findAllWithRoles();
}