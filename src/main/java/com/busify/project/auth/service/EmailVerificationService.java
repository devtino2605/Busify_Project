package com.busify.project.auth.service;

import com.busify.project.user.entity.Profile;

public interface EmailVerificationService {
    public void sendVerificationEmail(Profile user);

    public void verifyEmail(String token);

    public void resendVerificationEmail(String email);
}
