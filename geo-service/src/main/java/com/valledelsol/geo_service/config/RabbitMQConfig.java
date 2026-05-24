package com.valledelsol.geo_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ESTAS SON LAS 3 VARIABLES QUE EL COMPILADOR NO ENCUENTRA:
    public static final String INCIDENT_QUEUE = "incident.validation.queue";
    public static final String INCIDENT_EXCHANGE = "incident.exchange";
    public static final String INCIDENT_ROUTING_KEY = "incident.validated";

    @Bean
    public Queue incidentQueue() {
        return new Queue(INCIDENT_QUEUE, true);
    }

    @Bean
    public TopicExchange incidentExchange() {
        return new TopicExchange(INCIDENT_EXCHANGE);
    }

    @Bean
    public Binding incidentBinding(Queue incidentQueue, TopicExchange incidentExchange) {
        return BindingBuilder.bind(incidentQueue).to(incidentExchange).with(INCIDENT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}