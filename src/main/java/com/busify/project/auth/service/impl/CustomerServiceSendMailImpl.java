package com.busify.project.auth.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.busify.project.auth.service.CustomerServiceSendMail;
import com.busify.project.auth.service.EmailService;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.trip.dto.response.TripDetailResponse;
import com.busify.project.booking.entity.Bookings;

import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceSendMailImpl implements CustomerServiceSendMail {

        private final EmailService emailService;
        private final JwtUtils jwtUtils;
        private final UserRepository userRepository;
        private final TripRepository tripRepository;
        private final BookingRepository bookingRepository;

        @Override
        public void sendCustomerSupportEmail(String toEmail, String userName, String subject, String message,
                        String caseNumber, String csRepName) {

                try {
                        // Get current user for logging and auditing
                        String currentUserEmail = jwtUtils.getCurrentUserLogin()
                                        .orElseThrow(() -> new RuntimeException("User not authenticated"));

                        User currentUser = userRepository.findByEmail(currentUserEmail)
                                        .orElseThrow(() -> new RuntimeException("Current user not found"));

                        // check role
                        String role = currentUser.getRole().getName();
                        if (role.equals("ADMIN") || role.equals("OPERATOR")
                                        || role.equals("CUSTOMER_SERVICE")) {
                                throw new RuntimeException("User is not authorized to send support emails");
                        }
                        // Additional validation (most validations are done at DTO level with
                        // annotations)
                        if (toEmail == null || toEmail.trim().isEmpty()) {
                                throw new IllegalArgumentException("Recipient email cannot be empty");
                        }

                        // Send the actual email
                        emailService.sendCustomerSupportEmail(
                                        toEmail,
                                        userName,
                                        subject,
                                        message,
                                        caseNumber,
                                        csRepName);

                } catch (Exception e) {
                        // Log the error
                        throw new RuntimeException("Failed to send customer support email: " + e.getMessage(), e);
                }
        }

        @Override
        public void sendBulkCustomerSupportEmailByTrip(Long tripId, String subject, String message, String csRepName) {
                try {
                        // Get current user for logging and auditing
                        String currentUserEmail = jwtUtils.getCurrentUserLogin()
                                        .orElseThrow(() -> new RuntimeException("User not authenticated"));

                        User currentUser = userRepository.findByEmail(currentUserEmail)
                                        .orElseThrow(() -> new RuntimeException("Current user not found"));

                        // Validate trip exists
                        TripDetailResponse trip = tripRepository.findTripDetailById(tripId);
                        if (trip == null) {
                                throw new RuntimeException("Trip not found");
                        }

                        String route = trip.getStartCity() + " - " + trip.getEndCity();
                        String time = trip.getDepartureTime().toString() + " - "
                                        + trip.getEstimatedArrivalTime().toString();
                        String busCompany = trip.getOperatorName();

                        // Get all bookings for the trip
                        List<Bookings> bookings = bookingRepository.findByTripId(tripId);

                        // Collect unique emails from bookings (customers and guests)
                        Set<String> emails = new HashSet<>();
                        for (Bookings booking : bookings) {
                                if (booking.getCustomer() != null && booking.getCustomer().getEmail() != null) {
                                        emails.add(booking.getCustomer().getEmail());
                                }
                                if (booking.getGuestEmail() != null && !booking.getGuestEmail().isEmpty()) {
                                        emails.add(booking.getGuestEmail());
                                }
                        }

                        if (emails.isEmpty()) {
                                throw new RuntimeException("No emails found for trip ID: " + tripId);
                        }

                        // Send bulk emails
                        emailService.sendBulkCustomerSupportEmailByTrip(new ArrayList<>(emails), subject, message,
                                        csRepName, route, time, busCompany);

                } catch (Exception e) {
                        throw new RuntimeException("Failed to send bulk customer support email: " + e.getMessage(), e);
                }
        }

}
