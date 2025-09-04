package com.busify.project.notification.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.busify.project.notification.entity.NotificationData;
import com.busify.project.notification.repository.NotificationRepo;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepo notificationRepo;

    @MessageMapping("/message-received")
    public void receiveMessage(String nofiticationId) {
        notificationRepo.deleteById(nofiticationId);
    }

    public void sendMessage(NotificationData data) {
        messagingTemplate.convertAndSend("/topic/" + data.getId(), data.toMap());
    }
}