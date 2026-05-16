package com.valledelsol.incident_service.service;


import com.valledelsol.incident_service.model.Incident;
import com.valledelsol.incident_service.model.IncidentStatus;
import com.valledelsol.incident_service.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

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

    // 3. Buscar un incidente por ID (Agregamos validación de nulo para limpiar la advertencia 2)
    public Optional<Incident> getIncidentById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return incidentRepository.findById(id);
    }

    // 4. Actualizar estado/coordenadas (Refactorizado para limpiar la advertencia 1)
    public Incident updateIncident(String id, Double newLat, Double newLng, IncidentStatus newStatus) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        // Buscamos el incidente primero. Si no existe, lanza la excepción de inmediato.
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidente no encontrado con el ID: " + id));
        
        // Si existe, modificamos sus campos de forma segura
        if (newLat != null) incident.setLatitude(newLat);
        if (newLng != null) incident.setLongitude(newLng);
        if (newStatus != null) incident.setStatus(newStatus);
        
        // Guardamos y retornamos
        return incidentRepository.save(incident);
    }
}