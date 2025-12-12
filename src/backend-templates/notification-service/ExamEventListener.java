package com.smartuniversity.notification.listener;

import com.smartuniversity.notification.dto.ExamCreatedEvent;
import com.smartuniversity.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listens for exam events from Exam Service
 * Queue: exam-events
 */
@Component
public class ExamEventListener {
    
    private final NotificationService notificationService;
    
    public ExamEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    /**
     * Handles 'exam.created' events
     * Sends SMS/Email notifications to all students
     */
    @RabbitListener(queues = "exam-created-queue")
    public void handleExamCreated(ExamCreatedEvent event) {
        System.out.println("📬 Received exam.created event: " + event.getTitle());
        
        // Notify all students about the new exam
        notificationService.notifyAllStudentsAboutExam(
            event.getTitle(),
            event.getDate()
        );
        
        System.out.println("✅ Notifications sent for exam: " + event.getTitle());
    }
}
