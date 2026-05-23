package com.valledelsol.bff_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservice.incident.url}")
    private String incidentServiceUrl;

    // Creamos un cliente web configurado especialmente para apuntar al servicio de incidentes
    @Bean
    public WebClient incidentWebClient() {
        return WebClient.builder()
                .baseUrl(incidentServiceUrl)
                .build();
    }
}