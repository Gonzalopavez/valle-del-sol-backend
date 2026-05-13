package com.valledelsol.bff_service.controller;


import com.valledelsol.bff_service.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/ping")
    public ApiResponse<String> ping() {

        return new ApiResponse<>(
                true,
                "Bff Service funcionando correctamente",
                null
        );
    }
}