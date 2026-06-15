package com.valledelsol.geo_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valledelsol.geo_service.dto.ApiResponse;
import com.valledelsol.geo_service.dto.UserLocationRequest;
import com.valledelsol.geo_service.model.UserLocation;
import com.valledelsol.geo_service.service.LocationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas del GeoController con MockMvc.
 * Desactivamos los filtros de seguridad para probar solo el endpoint.
 */
@WebMvcTest(controllers = GeoController.class,
        excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
                type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
                classes = {
                        com.valledelsol.geo_service.security.JwtAuthenticationFilter.class,
                        com.valledelsol.geo_service.security.SecurityConfig.class
                }))
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas del GeoController")
class GeoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @Autowired
    private ObjectMapper objectMapper;

    // POST /api/v1/geo/location debe responder 201 Created.
    @Test
    @DisplayName("POST ubicacion responde 201")
    void guardarUbicacion_responde201() throws Exception {
        UserLocation guardada = UserLocation.builder()
                .id(1L).userId("vecino_1022")
                .latitude(-36.8269).longitude(-73.0498).deviceId("dev_1").build();
        ApiResponse<UserLocation> respuesta = ApiResponse.<UserLocation>builder()
                .message("Ubicacion procesada correctamente")
                .success(true).data(guardada).build();

        when(locationService.saveOrUpdateLocation(any(UserLocationRequest.class)))
                .thenReturn(respuesta);

        UserLocationRequest request = new UserLocationRequest();
        request.setUserId("vecino_1022");
        request.setLatitude(-36.8269);
        request.setLongitude(-73.0498);
        request.setDeviceId("dev_1");

        mockMvc.perform(post("/api/v1/geo/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value("vecino_1022"));
    }
}