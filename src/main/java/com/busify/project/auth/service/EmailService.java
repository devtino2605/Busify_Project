package com.busify.project.auth.service;

import com.busify.project.ticket.entity.Tickets;
import com.busify.project.user.entity.Profile;

import java.util.List;

public interface EmailService {

    public void sendVerificationEmail(Profile user, String token);

    public void sendPasswordResetEmail(Profile user, String token);

    void sendTicketEmail(String toEmail, String fullName, List<Tickets> tickets);

    void sendSimpleEmail(String toEmail, String subject, String content);

    void sendTicketCancelledEmail(String toEmail, String fullName, Tickets ticket);

    void sendBookingCancelledEmail(String toEmail, String fullName, List<Tickets> tickets);

    void sendBookingUpdatedEmail(String toEmail, String fullName, List<Tickets> tickets);

    void sendComplaintStatusEmail(String toEmail, String fullName, String complaintStatus, String complaintContent);
}