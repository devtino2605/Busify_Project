package com.busify.project.auth.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.busify.project.auth.entity.VerificationToken;
import com.busify.project.auth.enums.TokenType;
import com.busify.project.user.entity.User;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUserAndTokenTypeAndUsedFalse(User user, TokenType tokenType);

    void deleteByUserAndTokenType(User user, TokenType emailVerification);

    List<VerificationToken> findAllByExpiryDateBefore(LocalDateTime expiryDate);

    @Modifying
    @Query("DELETE FROM VerificationToken v WHERE v.expiryDate < :now")
    void deleteByExpiryDateBefore(@Param("now") LocalDateTime now);

}
