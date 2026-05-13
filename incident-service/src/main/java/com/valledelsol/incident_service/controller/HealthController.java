package com.valledelsol.incident_service.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valledelsol.incident_service.dto.ApiResponse;

@RestController
public class HealthController {

    @GetMapping("/ping")
    public ApiResponse<String> ping() {

        return new ApiResponse<>(
                true,
                "Base Service funcionando correctamente",
                null
        );
    }
}