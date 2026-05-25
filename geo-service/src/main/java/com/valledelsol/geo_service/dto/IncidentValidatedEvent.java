package com.valledelsol.geo_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncidentValidatedEvent {
    private String id;          
    private String description;
    private Double latitude;
    private Double longitude;
    private String status;
}