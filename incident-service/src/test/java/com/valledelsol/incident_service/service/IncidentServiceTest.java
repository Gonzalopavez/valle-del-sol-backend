
package com.valledelsol.incident_service.service;

import com.valledelsol.incident_service.config.RabbitMQConfig;
import com.valledelsol.incident_service.model.Incident;
import com.valledelsol.incident_service.model.IncidentStatus;
import com.valledelsol.incident_service.repository.IncidentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de IncidentService.
 * Usamos Mockito para imitar (mock) el repositorio MongoDB y RabbitTemplate,
 * asi probamos solo la logica de negocio sin levantar bases de datos reales.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de IncidentService")
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private IncidentService incidentService;

    private Incident incidentBase;

    @BeforeEach
    void setUp() {
        // Arrange comun: un incidente de ejemplo para varias pruebas.
        incidentBase = new Incident();
        incidentBase.setId("abc123");
        incidentBase.setUserId("vecino_1022");
        incidentBase.setDescription("Humo en el cerro");
        incidentBase.setLatitude(-36.8269);
        incidentBase.setLongitude(-73.0498);
        incidentBase.setImageUrl("http://foto.jpg");
    }

    // CRITERIO US-01: al crear un reporte, debe quedar en estado PENDING.
    @Test
    @DisplayName("Crear incidente lo deja en estado PENDING con fecha")
    void crearIncidente_quedaEnPending() {
        when(incidentRepository.save(any(Incident.class))).thenAnswer(inv -> inv.getArgument(0));

        Incident resultado = incidentService.createIncident(incidentBase);

        assertEquals(IncidentStatus.PENDING, resultado.getStatus());
        assertNotNull(resultado.getCreatedAt());
        verify(incidentRepository, times(1)).save(incidentBase);
    }

    // Validacion defensiva: no se puede crear un incidente nulo.
    @Test
    @DisplayName("Crear incidente nulo lanza excepcion")
    void crearIncidente_nulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> incidentService.createIncident(null));
        verify(incidentRepository, never()).save(any());
    }

    // CRITERIO US-02: el admin puede ver todos los reportes.
    @Test
    @DisplayName("Obtener todos devuelve la lista del repositorio")
    void obtenerTodos_devuelveLista() {
        Incident otro = new Incident();
        otro.setId("xyz789");
        when(incidentRepository.findAll()).thenReturn(Arrays.asList(incidentBase, otro));

        List<Incident> resultado = incidentService.getAllIncidents();

        assertEquals(2, resultado.size());
        verify(incidentRepository, times(1)).findAll();
    }

    // Buscar por id cuando existe.
    @Test
    @DisplayName("Buscar por id existente devuelve el incidente")
    void buscarPorId_existe() {
        when(incidentRepository.findById("abc123")).thenReturn(Optional.of(incidentBase));

        Optional<Incident> resultado = incidentService.getIncidentById("abc123");

        assertTrue(resultado.isPresent());
        assertEquals("vecino_1022", resultado.get().getUserId());
    }

    // Buscar por id nulo devuelve vacio.
    @Test
    @DisplayName("Buscar por id nulo devuelve vacio")
    void buscarPorId_nulo_devuelveVacio() {
        Optional<Incident> resultado = incidentService.getIncidentById(null);

        assertTrue(resultado.isEmpty());
        verify(incidentRepository, never()).findById(anyString());
    }

    // CRITERIO US-02: corregir coordenadas SIN validar NO debe publicar a RabbitMQ.
    @Test
    @DisplayName("Actualizar sin validar no publica evento en RabbitMQ")
    void actualizar_sinValidar_noPublicaEvento() {
        when(incidentRepository.findById("abc123")).thenReturn(Optional.of(incidentBase));
        when(incidentRepository.save(any(Incident.class))).thenAnswer(inv -> inv.getArgument(0));

        Incident resultado = incidentService.updateIncident("abc123", -36.0, -73.0, null);

        assertEquals(-36.0, resultado.getLatitude());
        assertEquals(-73.0, resultado.getLongitude());
        verify(rabbitTemplate, never())
                .convertAndSend(anyString(), anyString(), any(Incident.class));
    }

    // CRITERIO US-02 (importante): al validar, DEBE publicar el evento en RabbitMQ.
    @Test
    @DisplayName("Actualizar a VALIDATED publica evento en RabbitMQ")
    void actualizar_validado_publicaEvento() {
        when(incidentRepository.findById("abc123")).thenReturn(Optional.of(incidentBase));
        when(incidentRepository.save(any(Incident.class))).thenAnswer(inv -> inv.getArgument(0));

        Incident resultado = incidentService.updateIncident("abc123", null, null, IncidentStatus.VALIDATED);

        assertEquals(IncidentStatus.VALIDATED, resultado.getStatus());
        // Verificamos que se publico exactamente una vez al exchange y routing key correctos.
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_NAME),
                eq(RabbitMQConfig.ROUTING_KEY),
                any(Incident.class));
    }

    // Actualizar un incidente que no existe lanza excepcion.
    @Test
    @DisplayName("Actualizar incidente inexistente lanza excepcion")
    void actualizar_noExiste_lanzaExcepcion() {
        when(incidentRepository.findById("noexiste")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> incidentService.updateIncident("noexiste", -36.0, -73.0, IncidentStatus.VALIDATED));

        verify(incidentRepository, never()).save(any());
        verify(rabbitTemplate, never())
                .convertAndSend(anyString(), anyString(), any(Incident.class));
    }

    // Actualizar con id nulo lanza excepcion.
    @Test
    @DisplayName("Actualizar con id nulo lanza excepcion")
    void actualizar_idNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> incidentService.updateIncident(null, -36.0, -73.0, IncidentStatus.VALIDATED));
    }
}