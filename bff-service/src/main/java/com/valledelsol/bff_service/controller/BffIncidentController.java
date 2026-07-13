package com.valledelsol.bff_service.controller;

import com.valledelsol.bff_service.config.RabbitMQConfig;
import com.valledelsol.bff_service.dto.IncidentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final RabbitTemplate rabbitTemplate; // Herramienta para publicar en RabbitMQ

    // 1. POST: Crear incidente — ahora pasa por RabbitMQ para mayor resiliencia
    // Si el incident-service o MongoDB están caídos, el reporte queda en la cola
    // y se procesa cuando vuelvan a estar disponibles
    @PostMapping
    public Mono<String> createIncident(@RequestBody IncidentDTO incidentDTO) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.INCIDENT_REPORT_QUEUE,
            incidentDTO
        );
        System.out.println("[BFF - RabbitMQ] Reporte publicado en la cola de resiliencia: "
            + incidentDTO.getUserId());
        return Mono.just("Reporte recibido y encolado correctamente.");
    }

    // 2. GET: Obtener todos los incidentes
    @GetMapping
    public Flux<IncidentDTO> getAllIncidents() {
        return incidentWebClient.get()
                .uri("/api/incidents")
                .retrieve()
                .bodyToFlux(IncidentDTO.class);
    }

    // 3. PUT: Validar incidente (El Admin corrige y aprueba)
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

    // 4. DELETE: Cancelar/eliminar incidente
    @DeleteMapping("/{id}")
    public Mono<Void> deleteIncident(@PathVariable String id) {
        return incidentWebClient.delete()
                .uri("/api/incidents/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}