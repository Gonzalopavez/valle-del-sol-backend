package com.valledelsol.geo_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationRequest {
    private String userId;
    private Double latitude;
    private Double longitude;
    private String deviceId;
}