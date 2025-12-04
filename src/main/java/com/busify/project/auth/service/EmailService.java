package com.busify.project.auth.service;

import com.busify.project.cargo.entity.CargoBooking;
import com.busify.project.ticket.entity.Tickets;
import com.busify.project.trip.entity.Trip;
import com.busify.project.user.entity.Profile;

import java.util.List;

public interface EmailService {

        public void sendVerificationEmail(Profile user, String token);

        public void sendPasswordResetEmail(Profile user, String token);

        void sendTicketEmail(String toEmail, String fullName, List<Tickets> tickets);

        void sendSimpleEmail(String toEmail, String subject, String content);

        void sendTicketCancelledEmail(String toEmail, String fullName, Tickets ticket);

        void sendBookingCancelledEmail(String toEmail, String fullName, List<Tickets> tickets);

        void sendBookingCancelledWithRefundEmail(String toEmail, String fullName, List<Tickets> tickets,
                        String refundAmount, String refundStatus, String refundReason);

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
        void sendBulkCustomerSupportEmailByTrip(List<String> toEmails, String subject, String message, String csRepName,
                        String route, String time, String busCompany);

        void sendCustomerSupportEmailByTrip(String toEmail, String userName, String subject, String message,
                        String csRepName, String route, String time, String busCompany);

        void sendCustomerSupportEmailToBusOperator(String toEmail, String userName, String subject,
                        String message, String csRepName);

        /**
         * Sends a cargo booking confirmation email with PDF attachment
         *
         * @param cargoBooking  the cargo booking entity with sender/receiver info
         * @param pdfAttachment PDF byte array to attach to email
         */
        void sendCargoBookingConfirmationEmail(CargoBooking cargoBooking, byte[] pdfAttachment);

        /**
         * Sends a cargo rejection notification email
         *
         * @param cargoBooking    the cargo booking entity being rejected
         * @param rejectionReason the reason why cargo was rejected by staff
         */
        void sendCargoRejectionEmail(CargoBooking cargoBooking, String rejectionReason);

        /**
         * Sends a cargo refund notification email
         *
         * @param cargoBooking the cargo booking entity being refunded
         * @param refund       the refund entity with amount and transaction details
         */
        void sendCargoRefundEmail(CargoBooking cargoBooking, com.busify.project.refund.entity.Refund refund);

        /**
         * Sends a cargo arrival notification email with QR code for pickup verification
         *
         * @param cargoBooking the cargo booking entity that has arrived at destination
         * @param trip         the trip entity for route/time information
         * @param pickupToken  JWT token for QR code generation (valid for 7 days)
         */
        void sendCargoArrivalEmailWithQR(CargoBooking cargoBooking,
                        Trip trip,
                        String pickupToken);

        /**
         * Sends a trip cancellation notification email to customers
         *
         * @param toEmail            recipient's email address
         * @param customerName       customer's name
         * @param trip               the cancelled trip entity
         * @param cancellationReason reason for cancellation
         * @param refundInfo         refund information (amount, status)
         * @param isDelayed          true if trip is delayed (not cancelled)
         * @param newDepartureTime   new departure time (for delayed trips)
         */
        void sendTripCancellationEmail(String toEmail, String customerName, Trip trip,
                        String cancellationReason, String refundInfo, boolean isDelayed,
                        java.time.LocalDateTime newDepartureTime);

        /**
         * Sends bulk trip cancellation notification emails to multiple customers
         *
         * @param toEmails           list of recipient email addresses
         * @param trip               the cancelled trip entity
         * @param cancellationReason reason for cancellation
         * @param isDelayed          true if trip is delayed (not cancelled)
         * @param newDepartureTime   new departure time (for delayed trips)
         */
        void sendBulkTripCancellationEmail(List<String> toEmails, Trip trip,
                        String cancellationReason, boolean isDelayed,
                        java.time.LocalDateTime newDepartureTime);
}