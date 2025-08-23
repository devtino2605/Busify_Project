package com.busify.project.auth.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busify.project.auth.entity.VerificationToken;
import com.busify.project.auth.enums.TokenType;
import com.busify.project.auth.repository.VerificationTokenRepository;
import com.busify.project.auth.service.EmailService;
import com.busify.project.auth.service.EmailVerificationService;
import com.busify.project.common.exception.InvalidTokenException;
import com.busify.project.common.exception.TokenExpiredException;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public void sendVerificationEmail(Profile user) {
        // Xóa token cũ nếu có
        tokenRepository.deleteByUserAndTokenType(user, TokenType.EMAIL_VERIFICATION);

        // Tạo token mới
        String token = generateVerificationToken();
        VerificationToken verificationToken = new VerificationToken(token, user, TokenType.EMAIL_VERIFICATION);
        tokenRepository.save(verificationToken);

        // Gửi email
        emailService.sendVerificationEmail(user, token);
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
    }

    @Scheduled(fixedRate = 3600000) // Chạy mỗi giờ
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }

    @Override
    public void verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid verification token"));

        if (verificationToken.isExpired()) {
            throw new TokenExpiredException("Verification token has expired");
        }

        if (verificationToken.isUsed()) {
            throw new InvalidTokenException("Verification token already used");
        }

        if (verificationToken.getTokenType() != TokenType.EMAIL_VERIFICATION) {
            throw new InvalidTokenException("Invalid token type");
        }

        // Cập nhật user - cast an toàn từ User sang Profile
        User user = verificationToken.getUser();
        if (user instanceof Profile profile) {
            profile.setEmailVerified(true);
            userRepository.save(profile);
        } else {
            // Fallback cho trường hợp user không phải Profile
            user.setEmailVerified(true);
            userRepository.save(user);
        }

        // Đánh dấu token đã sử dụng
        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);
    }

    @Override
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException("User not found with email: " + email));

        if (!(user instanceof Profile)) {
            throw new InvalidTokenException("User is not a profile type");
        }

        Profile profile = (Profile) user;

        if (profile.isEmailVerified()) {
            throw new InvalidTokenException("Email is already verified");
        }

        sendVerificationEmail(profile);
    }

}
