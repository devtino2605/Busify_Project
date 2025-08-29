package com.busify.project.auth.service;

public interface CustomerServiceSendMail {

    void sendCustomerSupportEmail(String toEmail, String userName, String subject, String message, String caseNumber,
            String csRepName);
}