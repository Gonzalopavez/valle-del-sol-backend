package com.valledelsol.geo_service.service;

import com.valledelsol.geo_service.dto.ApiResponse;
import com.valledelsol.geo_service.dto.UserLocationRequest;
import com.valledelsol.geo_service.model.UserLocation;
import com.valledelsol.geo_service.repository.UserLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de LocationService.
 * Mockeamos el UserLocationRepository (PostgreSQL) para probar solo la logica:
 * el guardado/actualizacion de ubicaciones y el calculo de cercania (Haversine).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de LocationService")
class LocationServiceTest {

    @Mock
    private UserLocationRepository repository;

    @InjectMocks
    private LocationService locationService;

    private UserLocationRequest request;

    @BeforeEach
    void setUp() {
        request = new UserLocationRequest();
        request.setUserId("vecino_1022");
        request.setLatitude(-36.8269);
        request.setLongitude(-73.0498);
        request.setDeviceId("device_3344");
    }

    // CRITERIO US-03 (Paso 0): guardar la ubicacion de un vecino nuevo.
    @Test
    @DisplayName("Guardar ubicacion de usuario nuevo la persiste correctamente")
    void guardarUbicacion_usuarioNuevo() {
        // El usuario no existe aun: el repositorio devuelve vacio.
        when(repository.findByUserId("vecino_1022")).thenReturn(Optional.empty());
        when(repository.save(any(UserLocation.class))).thenAnswer(inv -> inv.getArgument(0));

        ApiResponse<UserLocation> respuesta = locationService.saveOrUpdateLocation(request);

        assertTrue(respuesta.isSuccess());
        assertEquals("vecino_1022", respuesta.getData().getUserId());
        assertEquals(-36.8269, respuesta.getData().getLatitude());
        verify(repository, times(1)).save(any(UserLocation.class));
    }

    // CRITERIO US-03: si el vecino ya existe, se SOBRESCRIBE su ubicacion (1 fila por usuario).
    @Test
    @DisplayName("Guardar ubicacion de usuario existente sobrescribe la anterior")
    void guardarUbicacion_usuarioExistente_sobrescribe() {
        UserLocation existente = UserLocation.builder()
                .id(5L)
                .userId("vecino_1022")
                .latitude(-36.0)
                .longitude(-73.0)
                .deviceId("device_viejo")
                .build();
        when(repository.findByUserId("vecino_1022")).thenReturn(Optional.of(existente));
        when(repository.save(any(UserLocation.class))).thenAnswer(inv -> inv.getArgument(0));

        ApiResponse<UserLocation> respuesta = locationService.saveOrUpdateLocation(request);

        // Mantiene el mismo id (no crea fila nueva), pero actualiza las coordenadas.
        assertEquals(5L, respuesta.getData().getId());
        assertEquals(-36.8269, respuesta.getData().getLatitude());
        assertEquals("device_3344", respuesta.getData().getDeviceId());
    }

    // CRITERIO US-03 (clave): el calculo de cercania filtra a los vecinos dentro del radio.
    @Test
    @DisplayName("Buscar usuarios cercanos incluye a los que estan dentro del radio")
    void buscarCercanos_incluyeLosCercanos() {
        // Vecino muy cerca del incendio (a metros).
        UserLocation cerca = UserLocation.builder()
                .userId("cerca").latitude(-36.8270).longitude(-73.0499).build();
        // Vecino muy lejos (otra ciudad, a cientos de km).
        UserLocation lejos = UserLocation.builder()
                .userId("lejos").latitude(-33.4489).longitude(-70.6693).build();

        when(repository.findAll()).thenReturn(Arrays.asList(cerca, lejos));

        // Incendio en estas coordenadas, radio de 1 km.
        List<UserLocation> resultado =
                locationService.buscarUsuariosCercanos(-36.8269, -73.0498, 1.0);

        // Solo el cercano debe quedar en la lista.
        assertEquals(1, resultado.size());
        assertEquals("cerca", resultado.get(0).getUserId());
    }

    // El calculo descarta a todos si nadie esta dentro del radio.
    @Test
    @DisplayName("Buscar usuarios cercanos devuelve vacio si todos estan lejos")
    void buscarCercanos_todosLejos_devuelveVacio() {
        UserLocation lejos1 = UserLocation.builder()
                .userId("l1").latitude(-33.4489).longitude(-70.6693).build();
        UserLocation lejos2 = UserLocation.builder()
                .userId("l2").latitude(-23.6509).longitude(-70.3975).build();

        when(repository.findAll()).thenReturn(Arrays.asList(lejos1, lejos2));

        List<UserLocation> resultado =
                locationService.buscarUsuariosCercanos(-36.8269, -73.0498, 1.0);

        assertTrue(resultado.isEmpty());
    }

    // Con un radio enorme, todos entran.
    @Test
    @DisplayName("Buscar usuarios cercanos con radio amplio incluye a todos")
    void buscarCercanos_radioAmplio_incluyeTodos() {
        UserLocation u1 = UserLocation.builder()
                .userId("u1").latitude(-36.8270).longitude(-73.0499).build();
        UserLocation u2 = UserLocation.builder()
                .userId("u2").latitude(-33.4489).longitude(-70.6693).build();

        when(repository.findAll()).thenReturn(Arrays.asList(u1, u2));

        // Radio de 1000 km: ambos caben.
        List<UserLocation> resultado =
                locationService.buscarUsuariosCercanos(-36.8269, -73.0498, 1000.0);

        assertEquals(2, resultado.size());
    }
}