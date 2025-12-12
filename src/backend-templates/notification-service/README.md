# Notification Service - Team Delta (Port 8085)

## Spring Boot Backend Implementation

This folder contains the backend code templates for the Notification Service.
Copy these files to your Spring Boot project in the `backend/notification-service` directory.

## Project Structure
```
com.smartuniversity.notification/
├── NotificationServiceApplication.java
├── config/
│   └── RabbitMQConfig.java
├── listener/
│   ├── UserEventListener.java
│   └── ExamEventListener.java
├── controller/
│   └── NotificationController.java
├── service/
│   └── NotificationService.java
└── dto/
    ├── UserRegisteredEvent.java
    └── ExamCreatedEvent.java
```

## Dependencies (pom.xml)
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
</dependencies>
```

## application.yml
```yaml
server:
  port: 8085

spring:
  application:
    name: notification-service
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```
