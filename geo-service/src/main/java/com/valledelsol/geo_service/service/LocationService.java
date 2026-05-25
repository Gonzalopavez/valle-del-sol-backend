package com.valledelsol.geo_service.service;

import com.valledelsol.geo_service.dto.ApiResponse;
import com.valledelsol.geo_service.dto.UserLocationRequest;
import com.valledelsol.geo_service.model.UserLocation;
import com.valledelsol.geo_service.repository.UserLocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final UserLocationRepository repository;

    public LocationService(UserLocationRepository repository) {
        this.repository = repository;
    }

    public ApiResponse<UserLocation> saveOrUpdateLocation(UserLocationRequest request) {
        UserLocation location = repository.findByUserId(request.getUserId())
                .orElse(new UserLocation());

        location.setUserId(request.getUserId());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setDeviceId(request.getDeviceId());

        UserLocation saved = repository.save(location);

        return ApiResponse.<UserLocation>builder()
                .message("Ubicación procesada correctamente")
                .success(true)
                .data(saved)
                .build();
    }
}