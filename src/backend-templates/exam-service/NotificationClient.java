package com.smartuniversity.exam.service;

import com.smartuniversity.exam.dto.ExamCreatedEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign Client for calling Notification Service directly
 * Used with Circuit Breaker pattern for resilience
 */
@FeignClient(name = "notification-service", url = "http://localhost:8085")
public interface NotificationClient {
    
    @PostMapping("/api/notifications/exam")
    void sendExamNotification(@RequestBody ExamCreatedEvent event);
}
