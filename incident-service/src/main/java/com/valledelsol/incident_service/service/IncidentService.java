package com.valledelsol.incident_service.service;


import com.valledelsol.incident_service.config.RabbitMQConfig;
import com.valledelsol.incident_service.model.Incident;
import com.valledelsol.incident_service.model.IncidentStatus;
import com.valledelsol.incident_service.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    
    private final RabbitTemplate rabbitTemplate; // herramienta de RabbitMQ

    // 1. Crear un nuevo reporte 
    public Incident createIncident(Incident incident) {
        if (incident == null) {
            throw new IllegalArgumentException("El incidente no puede ser nulo");
        }
        incident.setStatus(IncidentStatus.PENDING);
        incident.setCreatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    // 2. Obtener todos los incidentes 
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    // 3. Buscar un incidente por ID 
    public Optional<Incident> getIncidentById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return incidentRepository.findById(id);
    }

    // 4. Actualizar estado/coordenadas de un incidente (usado por el Admin)
    public Incident updateIncident(String id, Double newLat, Double newLng, IncidentStatus newStatus) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado con el ID: " + id));
        
        if (newLat != null) incident.setLatitude(newLat);
        if (newLng != null) incident.setLongitude(newLng);
        if (newStatus != null) incident.setStatus(newStatus);
        
        // Se guardan los cambios corregidos por el Admin en MongoDB
        Incident savedIncident = incidentRepository.save(incident);

        // REGLA DE NEGOCIO: Si el estado cambió a VALIDATED, se lanza evento asíncrono
        if (newStatus == IncidentStatus.VALIDATED) {
            System.out.println("[RabbitMQ] Despachando incidente validado al exchange...");
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME, 
                RabbitMQConfig.ROUTING_KEY, 
                savedIncident // Spring lo transforma automáticamente a JSON gracias a nuestro convertidor
            );
            
            System.out.println(" [RabbitMQ] Incidente enviado con éxito a la cola de Geo-Service.");
        }
        
        return savedIncident;
    }
}