package com.busify.project.auth.service;

public interface CustomerServiceSendMail {

    void sendCustomerSupportEmail(String toEmail, String userName, String subject, String message, String caseNumber,
            String csRepName);

    /**
     * Sends bulk customer support emails to all users who booked tickets for a
     * specific trip
     *
     * @param tripId    the ID of the trip
     * @param subject   email subject
     * @param message   main email message content
     * @param csRepName customer service representative's name
     */
    void sendBulkCustomerSupportEmailByTrip(Long tripId, String subject, String message, String csRepName);

    /**
     * Sends bulk customer support emails to all bus operators for a specific trip
     *
     * @param tripId    the ID of the trip
     * @param subject   email subject
     * @param message   main email message content
     * @param csRepName customer service representative's name
     */
    void sendCustomerSupportEmailToBusOperator(Long tripId, String subject, String message, String csRepName);
}