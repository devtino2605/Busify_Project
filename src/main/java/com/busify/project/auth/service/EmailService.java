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

    /**
     * Sends a customer support email from a customer service representative to a
     * user
     *
     * @param toEmail    recipient's email address
     * @param userName   recipient's name
     * @param subject    email subject
     * @param message    main email message content
     * @param caseNumber support case reference number (optional)
     * @param csRepName  customer service representative's name
     */
    void sendCustomerSupportEmail(String toEmail, String userName, String subject,
            String message, String caseNumber, String csRepName);

    /**
     * Sends bulk customer support emails to multiple recipients
     *
     * @param toEmails  list of recipient email addresses
     * @param subject   email subject
     * @param message   main email message content
     * @param csRepName customer service representative's name
     */
    void sendBulkCustomerSupportEmail(List<String> toEmails, String subject, String message, String csRepName);
}