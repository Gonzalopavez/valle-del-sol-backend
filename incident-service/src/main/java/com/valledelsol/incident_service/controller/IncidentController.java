package com.valledelsol.incident_service.controller;


import com.valledelsol.incident_service.model.Incident;
import com.valledelsol.incident_service.model.IncidentStatus;
import com.valledelsol.incident_service.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor // Inyecta el Service de forma automatica mediante constructor
public class IncidentController {

    private final IncidentService incidentService;

    // 1. POST - Crear Reporte (US-01: Vecino reporta humo)
    // URL: POST http://localhost:8081/api/incidents
    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        Incident created = incidentService.createIncident(incident);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // 2. GET - Obtener todos los reportes (Para el mapa del Administrador)
    // URL: GET http://localhost:8081/api/incidents
    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        List<Incident> incidents = incidentService.getAllIncidents();
        return ResponseEntity.ok(incidents);
    }

    // 3. GET - Buscar un reporte por su ID
    // URL: GET http://localhost:8081/api/incidents/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable String id) {
        return incidentService.getIncidentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. PUT - Validar/Modificar Incidente (Fase 3: Admin corrige coordenadas y valida)
    // URL: PUT http://localhost:8081/api/incidents/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIncident(
            @PathVariable String id,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) IncidentStatus status) {
        try {
            Incident updated = incidentService.updateIncident(id, latitude, longitude, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}