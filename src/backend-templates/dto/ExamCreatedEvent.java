package com.smartuniversity.notification.dto;

/**
 * DTO for exam.created events
 * Published by Exam Service, consumed by Notification Service
 */
public class ExamCreatedEvent {
    
    private String examId;
    private String title;
    private String date;
    
    // Default constructor for JSON deserialization
    public ExamCreatedEvent() {}
    
    public ExamCreatedEvent(String examId, String title, String date) {
        this.examId = examId;
        this.title = title;
        this.date = date;
    }
    
    // Getters and Setters
    public String getExamId() {
        return examId;
    }
    
    public void setExamId(String examId) {
        this.examId = examId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
}
