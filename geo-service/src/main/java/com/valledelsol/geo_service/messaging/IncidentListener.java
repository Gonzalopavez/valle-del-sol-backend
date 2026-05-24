package com.valledelsol.geo_service.messaging;

import com.valledelsol.geo_service.config.RabbitMQConfig;
import com.valledelsol.geo_service.dto.IncidentValidatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class IncidentListener {

    @RabbitListener(queues = RabbitMQConfig.INCIDENT_QUEUE)
    public void handleIncidentValidated(IncidentValidatedEvent event) {
        System.out.println("=== EVENTO RECIBIDO DESDE EL RABBITMQ DE GONZALO ===");
        System.out.println("Incidente ID: " + event.getIncidentId());
        System.out.println("Estado de Validación: " + event.getStatus());
    }
}