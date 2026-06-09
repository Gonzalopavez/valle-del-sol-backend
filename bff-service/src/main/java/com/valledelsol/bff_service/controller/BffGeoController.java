package com.valledelsol.bff_service.controller;

import com.valledelsol.bff_service.dto.UserLocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bff/geo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BffGeoController {

    private final WebClient geoWebClient;

    // POST: el vecino envía su ubicación. El BFF la reenvía a geo-service.
    @PostMapping("/location")
    public Mono<Object> updateLocation(@RequestBody UserLocationDTO locationDTO) {
        return geoWebClient.post()
                .uri("/api/v1/geo/location")
                .bodyValue(locationDTO)
                .retrieve()
                .bodyToMono(Object.class);
    }
}