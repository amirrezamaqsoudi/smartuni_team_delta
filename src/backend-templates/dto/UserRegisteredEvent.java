package com.smartuniversity.notification.dto;

/**
 * DTO for user.registered events
 * Published by Auth Service (Team Alpha), consumed by Notification Service
 */
public class UserRegisteredEvent {
    
    private String userId;
    private String email;
    
    // Default constructor for JSON deserialization
    public UserRegisteredEvent() {}
    
    public UserRegisteredEvent(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
