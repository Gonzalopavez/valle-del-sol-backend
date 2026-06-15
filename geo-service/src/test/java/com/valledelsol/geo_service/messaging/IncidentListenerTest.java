package com.valledelsol.geo_service.messaging;

import com.valledelsol.geo_service.dto.IncidentValidatedEvent;
import com.valledelsol.geo_service.model.UserLocation;
import com.valledelsol.geo_service.service.LocationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

/**
 * Pruebas del IncidentListener (consumidor de RabbitMQ).
 * Mockeamos LocationService para verificar que, al recibir un evento de
 * incidente validado, el listener consulta los usuarios cercanos.
 * No levantamos RabbitMQ: llamamos al metodo directamente.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas del IncidentListener (RabbitMQ)")
class IncidentListenerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private IncidentListener incidentListener;

    private IncidentValidatedEvent eventoEjemplo() {
        IncidentValidatedEvent ev = new IncidentValidatedEvent();
        ev.setId("inc123");
        ev.setDescription("Incendio validado");
        ev.setLatitude(-36.8269);
        ev.setLongitude(-73.0498);
        ev.setStatus("VALIDATED");
        return ev;
    }

    // Al recibir el evento, debe consultar usuarios cercanos con las coordenadas del incidente.
    @Test
    @DisplayName("Al recibir evento validado consulta usuarios cercanos")
    void recibeEvento_consultaCercanos() {
        UserLocation cercano = UserLocation.builder()
                .userId("vecino_1").deviceId("dev_1")
                .latitude(-36.8270).longitude(-73.0499).build();
        when(locationService.buscarUsuariosCercanos(anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Arrays.asList(cercano));

        incidentListener.handleIncidentValidated(eventoEjemplo());

        // Verificamos que se consulto el servicio con las coordenadas del evento.
        verify(locationService, times(1))
                .buscarUsuariosCercanos(eq(-36.8269), eq(-73.0498), anyDouble());
    }

    // Tambien debe funcionar cuando no hay nadie cerca (lista vacia).
    @Test
    @DisplayName("Al recibir evento sin usuarios cercanos no falla")
    void recibeEvento_sinCercanos() {
        when(locationService.buscarUsuariosCercanos(anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Collections.emptyList());

        incidentListener.handleIncidentValidated(eventoEjemplo());

        verify(locationService, times(1))
                .buscarUsuariosCercanos(anyDouble(), anyDouble(), anyDouble());
    }
}