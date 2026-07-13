package com.valledelsol.incident_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ── Cola existente: incident-service → geo-service ──────────────────────
    public static final String EXCHANGE_NAME = "incident.exchange";
    public static final String QUEUE_NAME = "geo.incident.queue";
    public static final String ROUTING_KEY = "incident.validated";

    // 1. Exchange existente
    @Bean
    public TopicExchange incidentExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 2. Cola existente donde geo-service escucha
    @Bean
    public Queue geoQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    // 3. Binding existente entre cola y exchange
    @Bean
    public Binding bindingGeo(Queue geoQueue, TopicExchange incidentExchange) {
        return BindingBuilder.bind(geoQueue).to(incidentExchange).with(ROUTING_KEY);
    }

    // ── Cola nueva: BFF → incident-service (resiliencia de reportes) ─────────
    public static final String INCIDENT_REPORT_QUEUE = "incident.report.queue";

    // 4. Cola nueva donde incident-service escuchará los reportes del BFF
    @Bean
    public Queue incidentReportQueue() {
        return new Queue(INCIDENT_REPORT_QUEUE, true);
    }

    // ── Convertidor compartido ────────────────────────────────────────────────
    // 5. Convertidor JSON — usado tanto para publicar como para consumir
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}