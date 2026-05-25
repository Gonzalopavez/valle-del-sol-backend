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
    private String incidentId;
    private String status;
    private String validatorName;
    private String timestamp;
}