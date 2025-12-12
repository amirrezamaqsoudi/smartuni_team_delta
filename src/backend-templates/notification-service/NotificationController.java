package com.smartuniversity.notification.controller;

import com.smartuniversity.notification.dto.ExamCreatedEvent;
import com.smartuniversity.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for direct notification calls
 * Used by Exam Service via Feign Client with Circuit Breaker
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    /**
     * POST /api/notifications/exam
     * Direct endpoint for exam notifications (called by Exam Service with Circuit Breaker)
     */
    @PostMapping("/exam")
    public ResponseEntity<String> sendExamNotification(@RequestBody ExamCreatedEvent event) {
        notificationService.notifyAllStudentsAboutExam(event.getTitle(), event.getDate());
        return ResponseEntity.ok("Notifications sent for exam: " + event.getTitle());
    }
    
    /**
     * GET /api/notifications/health
     * Health check endpoint for Circuit Breaker monitoring
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Notification Service is healthy");
    }
}
