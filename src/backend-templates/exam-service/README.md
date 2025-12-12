# Exam Service - Team Delta (Port 8084)

## Spring Boot Backend Implementation

This folder contains the backend code templates for the Exam Service.
Copy these files to your Spring Boot project in the `backend/exam-service` directory.

## Project Structure
```
com.smartuniversity.exam/
├── ExamServiceApplication.java
├── config/
│   └── RabbitMQConfig.java
├── controller/
│   └── ExamController.java
├── entity/
│   └── Exam.java
├── repository/
│   └── ExamRepository.java
├── service/
│   ├── ExamService.java
│   └── NotificationClient.java
└── dto/
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
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <dependency>
        <groupId>io.github.resilience4j</groupId>
        <artifactId>resilience4j-spring-boot3</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

## application.yml
```yaml
server:
  port: 8084

spring:
  application:
    name: exam-service
  datasource:
    url: jdbc:postgresql://localhost:5432/smartuniversity
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

resilience4j:
  circuitbreaker:
    instances:
      notificationService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        permittedNumberOfCallsInHalfOpenState: 3
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 5s
        failureRateThreshold: 50
```
