package com.valledelsol.geo_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_locations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String userId;
    private Double latitude;
    private Double longitude;
    private String deviceId;
}