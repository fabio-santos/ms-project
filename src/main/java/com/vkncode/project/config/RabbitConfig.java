package com.vkncode.project.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static String EXCHANGE_NAME = "project-exchange";
    public static final String SECRETARIAT_VALIDATION_QUEUE = "secretariat-validation-queue";
    public static final String ROUTING_KEY_PROJECT_TO_SECRETARIAT = "project-to-secretariat";
    public static final String BUDGET_VALIDATION_QUEUE = "budget-validation-queue";
    public static final String ROUTING_KEY_SECRETARIAT_TO_BUDGET = "secretariat-to-budget";
    public static final String PROJECT_RESPONSE_QUEUE = "project-response-queue";
    public static final String ROUTING_KEY_ANY_TO_PROJECT = "any-to-project";

    @Bean
    public Exchange declareExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    @Bean
    public Queue declareQueueBudget() {
        return QueueBuilder.durable(BUDGET_VALIDATION_QUEUE)
                .build();
    }

    @Bean
    public Binding declareBindingBudget(Exchange exchange) {
        return BindingBuilder.bind(declareQueueBudget())
                .to(exchange)
                .with(ROUTING_KEY_SECRETARIAT_TO_BUDGET)
                .noargs();
    }

    @Bean
    public Queue declareQueueResponse() {
        return QueueBuilder.durable(PROJECT_RESPONSE_QUEUE)
                .build();
    }

    @Bean
    public Binding declareBindingResponse(Exchange exchange) {
        return BindingBuilder.bind(declareQueueResponse())
                .to(exchange)
                .with(ROUTING_KEY_ANY_TO_PROJECT)
                .noargs();
    }

    @Bean
    public Queue declareQueueSecretariat() {
        return QueueBuilder.durable(SECRETARIAT_VALIDATION_QUEUE)
                .build();
    }

    @Bean
    public Binding declareBindingSecretariat(Exchange exchange) {
        return BindingBuilder.bind(declareQueueSecretariat())
                .to(exchange)
                .with(ROUTING_KEY_PROJECT_TO_SECRETARIAT)
                .noargs();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(RabbitConfig.EXCHANGE_NAME);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
