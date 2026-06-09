package com.valledelsol.bff_service.dto;


import lombok.Data;

@Data
public class UserLocationDTO {
    private String userId;
    private Double latitude;
    private Double longitude;
    private String deviceId;
}