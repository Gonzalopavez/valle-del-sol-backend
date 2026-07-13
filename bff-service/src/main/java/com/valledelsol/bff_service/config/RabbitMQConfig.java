package com.valledelsol.bff_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombre de la cola de resiliencia de reportes
    // El BFF publica aquí y el incident-service escucha aquí
    public static final String INCIDENT_REPORT_QUEUE = "incident.report.queue";

    // Cola durable: si RabbitMQ se reinicia, la cola y sus mensajes sobreviven
    @Bean
    public Queue incidentReportQueue() {
        return new Queue(INCIDENT_REPORT_QUEUE, true);
    }

    // Convertidor para que los objetos viajen como JSON y no como bytes
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}