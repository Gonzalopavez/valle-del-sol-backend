package com.valledelsol.bff_service.controller;

import com.valledelsol.bff_service.dto.IncidentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bff/incidents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Clave para que el Frontend de la app no tenga errores de CORS
public class BffIncidentController {

    private final WebClient incidentWebClient;

    // 1. POST: Crear incidente (El vecino reporta)
    @PostMapping
    public Mono<IncidentDTO> createIncident(@RequestBody IncidentDTO incidentDTO) {
        return incidentWebClient.post()
                .uri("/api/incidents")
                .bodyValue(incidentDTO)
                .retrieve()
                .bodyToMono(IncidentDTO.class);
    }

    // 2.  GET: Obtener todos los incidentes
    @GetMapping
    public Flux<IncidentDTO> getAllIncidents() {
        return incidentWebClient.get()
                .uri("/api/incidents")
                .retrieve()
                .bodyToFlux(IncidentDTO.class);
    }

    // 3. S PUT: Validar incidente (El Admin corrige y aprueba)
    @PutMapping("/{id}")
    public Mono<IncidentDTO> updateIncident(
            @PathVariable String id,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String status) {
        
        // Reconstruimos la URL con los parámetros dinámicos que vienen del Frontend
        return incidentWebClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/incidents/{id}")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("status", status)
                        .build(id))
                .retrieve()
                .bodyToMono(IncidentDTO.class);
    }
}