package com.valledelsol.geo_service.service;

import com.valledelsol.geo_service.dto.ApiResponse;
import com.valledelsol.geo_service.dto.UserLocationRequest;
import com.valledelsol.geo_service.model.UserLocation;
import com.valledelsol.geo_service.repository.UserLocationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final UserLocationRepository repository;
    private static final double RADIO_TIERRA_KM = 6371.0; // Constante para el cálculo

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

    //MÉTODO: Busca usuarios a la redonda usando PostgreSQL
    public List<UserLocation> buscarUsuariosCercanos(Double incLat, Double incLng, Double radioMaxKm) {
        List<UserLocation> todosLosUsuarios = repository.findAll();
        
        return todosLosUsuarios.stream()
                .filter(user -> calcularDistanciaKm(incLat, incLng, user.getLatitude(), user.getLongitude()) <= radioMaxKm)
                .collect(Collectors.toList());
    }

    // Fórmula matemática de Haversine para calcular distancia entre dos coordenadas
    private double calcularDistanciaKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA_KM * c;
    }
}