package com.busify.project.user.repository;

import com.busify.project.user.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    
    @Query("SELECT p FROM Profile p WHERE p.id = :userId")
    Optional<Profile> findByUserId(@Param("userId") Long userId);
    
    Optional<Profile> findByEmail(String email);
}
