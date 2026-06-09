package com.valledelsol.bff_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservice.incident.url}")
    private String incidentServiceUrl;

    @Value("${microservice.geo.url}")
    private String geoServiceUrl;

    // Cliente web que apunta al servicio de incidentes
    @Bean
    public WebClient incidentWebClient() {
        return WebClient.builder()
                .baseUrl(incidentServiceUrl)
                .build();
    }

    // Cliente web que apunta al servicio de geolocalización
    @Bean
    public WebClient geoWebClient() {
        return WebClient.builder()
                .baseUrl(geoServiceUrl)
                .build();
    }
}