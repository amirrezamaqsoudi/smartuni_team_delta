package com.smartuniversity.exam.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String EXCHANGE_NAME = "exam-events";
    public static final String QUEUE_NAME = "exam-created-queue";
    public static final String ROUTING_KEY = "exam.created";
    
    @Bean
    public TopicExchange examExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
    
    @Bean
    public Queue examCreatedQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }
    
    @Bean
    public Binding examBinding(Queue examCreatedQueue, TopicExchange examExchange) {
        return BindingBuilder
            .bind(examCreatedQueue)
            .to(examExchange)
            .with(ROUTING_KEY);
    }
    
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
