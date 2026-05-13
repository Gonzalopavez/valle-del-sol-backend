package com.valledelsol.geo_service.controller;


import com.valledelsol.geo_service.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/ping")
    public ApiResponse<String> ping() {

        return new ApiResponse<>(
                true,
                "Geo Service funcionando correctamente",
                null
        );
    }
}