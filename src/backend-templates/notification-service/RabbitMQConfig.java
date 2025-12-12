package com.smartuniversity.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    // User Events (from Auth Service - Team Alpha)
    public static final String USER_EXCHANGE = "user-events";
    public static final String USER_REGISTERED_QUEUE = "user-registered-queue";
    public static final String USER_REGISTERED_KEY = "user.registered";
    
    // Exam Events (from Exam Service - Team Delta)
    public static final String EXAM_EXCHANGE = "exam-events";
    public static final String EXAM_CREATED_QUEUE = "exam-created-queue";
    public static final String EXAM_CREATED_KEY = "exam.created";
    
    // User Events Configuration
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }
    
    @Bean
    public Queue userRegisteredQueue() {
        return QueueBuilder.durable(USER_REGISTERED_QUEUE).build();
    }
    
    @Bean
    public Binding userBinding(Queue userRegisteredQueue, TopicExchange userExchange) {
        return BindingBuilder
            .bind(userRegisteredQueue)
            .to(userExchange)
            .with(USER_REGISTERED_KEY);
    }
    
    // Exam Events Configuration
    @Bean
    public TopicExchange examExchange() {
        return new TopicExchange(EXAM_EXCHANGE);
    }
    
    @Bean
    public Queue examCreatedQueue() {
        return QueueBuilder.durable(EXAM_CREATED_QUEUE).build();
    }
    
    @Bean
    public Binding examBinding(Queue examCreatedQueue, TopicExchange examExchange) {
        return BindingBuilder
            .bind(examCreatedQueue)
            .to(examExchange)
            .with(EXAM_CREATED_KEY);
    }
    
    // Message Converter
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
