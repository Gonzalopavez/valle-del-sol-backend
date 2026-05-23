package com.valledelsol.bff_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncidentDTO {
    private String id;
    private String userId;
    private String description;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private String status; 
    private LocalDateTime createdAt;
}
