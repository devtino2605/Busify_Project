package com.busify.project.auth.service;

import com.busify.project.user.entity.Profile;

public interface EmailService {

    public void sendVerificationEmail(Profile user, String token);

    public void sendPasswordResetEmail(Profile user, String token);
}