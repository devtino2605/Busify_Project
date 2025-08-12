package com.busify.project.auth.service;

import com.busify.project.ticket.entity.Tickets;
import com.busify.project.user.entity.Profile;

import java.util.List;

public interface EmailService {

    public void sendVerificationEmail(Profile user, String token);

    public void sendPasswordResetEmail(Profile user, String token);

    void sendTicketEmail(String toEmail, String fullName, List<Tickets> tickets);
}