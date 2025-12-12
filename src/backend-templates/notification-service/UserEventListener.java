package com.smartuniversity.notification.listener;

import com.smartuniversity.notification.dto.UserRegisteredEvent;
import com.smartuniversity.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listens for user registration events from Auth Service
 * Queue: user-events
 */
@Component
public class UserEventListener {
    
    private final NotificationService notificationService;
    
    public UserEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    /**
     * Handles 'user.registered' events
     * Sends welcome email to newly registered users
     */
    @RabbitListener(queues = "user-registered-queue")
    public void handleUserRegistered(UserRegisteredEvent event) {
        System.out.println("📬 Received user.registered event for: " + event.getEmail());
        
        notificationService.sendEmail(
            event.getEmail(),
            "Welcome to Smart University Platform!",
            "Hello! Your account has been created successfully. " +
            "User ID: " + event.getUserId() + ". " +
            "You can now access all university services."
        );
    }
}
