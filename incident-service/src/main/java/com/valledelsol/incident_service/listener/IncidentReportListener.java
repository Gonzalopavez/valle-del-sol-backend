package com.valledelsol.incident_service.listener;

import com.valledelsol.incident_service.config.RabbitMQConfig;
import com.valledelsol.incident_service.model.Incident;
import com.valledelsol.incident_service.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncidentReportListener {

    private final IncidentService incidentService;

    // Escucha la cola de resiliencia donde el BFF publica los reportes nuevos
    // Si este listener estaba caído, cuando vuelva procesa todos los mensajes acumulados
    @RabbitListener(queues = RabbitMQConfig.INCIDENT_REPORT_QUEUE)
    public void recibirReporte(Incident incident) {
        System.out.println("[incident-service - RabbitMQ] Reporte recibido desde la cola: "
            + incident.getUserId());
        incidentService.createIncident(incident);
        System.out.println("[incident-service - RabbitMQ] Reporte guardado en MongoDB correctamente.");
    }
}