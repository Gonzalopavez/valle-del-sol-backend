package com.valledelsol.geo_service.controller;

import com.valledelsol.geo_service.dto.ApiResponse;
import com.valledelsol.geo_service.dto.UserLocationRequest;
import com.valledelsol.geo_service.model.UserLocation;
import com.valledelsol.geo_service.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/geo")
public class GeoController {

    private final LocationService locationService;

    public GeoController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/location")
    public ResponseEntity<ApiResponse<UserLocation>> updateLocation(@RequestBody UserLocationRequest request) {
        ApiResponse<UserLocation> response = locationService.saveOrUpdateLocation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}