package com.smartuniversity.notification.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    /**
     * Simulates sending an email notification
     * In production: use JavaMailSender or external service like SendGrid
     */
    public void sendEmail(String to, String subject, String body) {
        System.out.println("===========================================");
        System.out.println("📧 SIMULATING EMAIL");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("===========================================");
        
        // Production implementation:
        // MimeMessage message = mailSender.createMimeMessage();
        // MimeMessageHelper helper = new MimeMessageHelper(message, true);
        // helper.setTo(to);
        // helper.setSubject(subject);
        // helper.setText(body, true);
        // mailSender.send(message);
    }
    
    /**
     * Simulates sending an SMS notification
     * In production: use Twilio or similar service
     */
    public void sendSMS(String phoneNumber, String message) {
        System.out.println("===========================================");
        System.out.println("📱 SIMULATING SMS");
        System.out.println("To: " + phoneNumber);
        System.out.println("Message: " + message);
        System.out.println("===========================================");
        
        // Production implementation with Twilio:
        // Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        // Message.creator(
        //     new PhoneNumber(phoneNumber),
        //     new PhoneNumber(FROM_NUMBER),
        //     message
        // ).create();
    }
    
    /**
     * Sends notification to all students about an exam
     */
    public void notifyAllStudentsAboutExam(String examTitle, String examDate) {
        // In production: fetch all student emails/phones from User Service
        String[] mockStudents = {"student1@university.edu", "student2@university.edu"};
        
        for (String student : mockStudents) {
            sendEmail(
                student,
                "New Exam Scheduled: " + examTitle,
                "A new exam has been scheduled for " + examDate + ". Please prepare accordingly."
            );
        }
        
        System.out.println("📢 Simulating SMS to all students for exam: " + examTitle);
    }
}
