package com.smartuniversity.exam.service;

import com.smartuniversity.exam.entity.Exam;
import com.smartuniversity.exam.repository.ExamRepository;
import com.smartuniversity.exam.dto.ExamCreatedEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExamService {
    
    private final ExamRepository examRepository;
    private final RabbitTemplate rabbitTemplate;
    private final NotificationClient notificationClient;
    
    private static final String EXCHANGE_NAME = "exam-events";
    private static final String ROUTING_KEY = "exam.created";
    
    public ExamService(ExamRepository examRepository, 
                       RabbitTemplate rabbitTemplate,
                       NotificationClient notificationClient) {
        this.examRepository = examRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.notificationClient = notificationClient;
    }
    
    /**
     * Creates an exam and publishes event to RabbitMQ
     * Also calls Notification Service directly with Circuit Breaker
     */
    public Exam createExam(String title, LocalDate date) {
        // Save exam to database
        Exam exam = new Exam(title, date);
        exam = examRepository.save(exam);
        
        // Create event payload
        ExamCreatedEvent event = new ExamCreatedEvent(
            exam.getId().toString(),
            exam.getTitle(),
            exam.getDate().toString()
        );
        
        // Publish to RabbitMQ (async notification)
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event);
        
        // Also try direct call with Circuit Breaker (Phase 3)
        notifyStudentsWithCircuitBreaker(event);
        
        return exam;
    }
    
    /**
     * Circuit Breaker protected call to Notification Service
     * Fallback: logs message for later retry
     */
    @CircuitBreaker(name = "notificationService", fallbackMethod = "notificationFallback")
    public void notifyStudentsWithCircuitBreaker(ExamCreatedEvent event) {
        notificationClient.sendExamNotification(event);
    }
    
    /**
     * Fallback method when Notification Service is unavailable
     */
    public void notificationFallback(ExamCreatedEvent event, Throwable t) {
        System.out.println("Notification service unavailable, queuing for later: " + event.getTitle());
        // In production: save to a retry queue or dead letter queue
    }
    
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }
    
    public Optional<Exam> getExamById(String id) {
        return examRepository.findById(UUID.fromString(id));
    }
}
