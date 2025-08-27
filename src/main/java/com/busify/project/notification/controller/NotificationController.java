package com.busify.project.notification.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRepository userRepository;

    public void notifyPaymentToOperator(Long userId, Long operatorId) {
        final User user = userRepository.findById(userId).orElseThrow();
        final Profile userProfile = (Profile) user;
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userProfile.getId());
        payload.put("message", "Passenger " + userProfile.getFullName() + " has completed the payment.");
        messagingTemplate.convertAndSend("/topic/operator/" + operatorId, payload);
    }

    public void notifyPaymentToOperator(Long operatorId, String userEmail) {
        final User user = userRepository.findByEmail(userEmail).orElseThrow();
        final Profile userProfile = (Profile) user;
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userProfile.getId());
        payload.put("email", userEmail);
        payload.put("message", "Passenger " + userProfile.getFullName() + " has completed the payment.");
        messagingTemplate.convertAndSend("/topic/operator/" + operatorId, payload);
    }

    public void notifyPaymentToOperator(Long operatorId, String userEmail, String phoneNumber) {
        Logger logger = Logger.getLogger(NotificationController.class.getName());
        logger.info("Notifying operator " + operatorId + " about payment from user " + userEmail);
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userEmail);
        payload.put("phoneNumber", phoneNumber);
        payload.put("email", userEmail);
        payload.put("message", "Passenger " + userEmail + " has completed the payment.");
        messagingTemplate.convertAndSend("/topic/operator/" + operatorId, payload);
    }

}
