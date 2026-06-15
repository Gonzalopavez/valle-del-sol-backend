package com.valledelsol.incident_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valledelsol.incident_service.model.Incident;
import com.valledelsol.incident_service.model.IncidentStatus;
import com.valledelsol.incident_service.service.IncidentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas del IncidentController usando MockMvc.
 * @WebMvcTest levanta SOLO la capa web (el controller), sin MongoDB ni RabbitMQ.
 * El IncidentService se reemplaza por un mock con @MockitoBean.
 * Aqui probamos que cada endpoint responda con el codigo HTTP correcto.
 */
@WebMvcTest(IncidentController.class)
@DisplayName("Pruebas del IncidentController")
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IncidentService incidentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Incident incidentEjemplo() {
        Incident inc = new Incident();
        inc.setId("abc123");
        inc.setUserId("vecino_1022");
        inc.setDescription("Humo en el cerro");
        inc.setLatitude(-36.8269);
        inc.setLongitude(-73.0498);
        inc.setStatus(IncidentStatus.PENDING);
        return inc;
    }

    // POST /api/incidents debe responder 201 Created.
    @Test
    @DisplayName("POST crea incidente y responde 201")
    void crearIncidente_responde201() throws Exception {
        Incident inc = incidentEjemplo();
        when(incidentService.createIncident(any(Incident.class))).thenReturn(inc);

        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inc)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    // GET /api/incidents debe responder 200 con la lista.
    @Test
    @DisplayName("GET todos responde 200 con la lista")
    void obtenerTodos_responde200() throws Exception {
        List<Incident> lista = Arrays.asList(incidentEjemplo(), incidentEjemplo());
        when(incidentService.getAllIncidents()).thenReturn(lista);

        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // GET /api/incidents/{id} cuando existe responde 200.
    @Test
    @DisplayName("GET por id existente responde 200")
    void obtenerPorId_existe_responde200() throws Exception {
        when(incidentService.getIncidentById("abc123")).thenReturn(Optional.of(incidentEjemplo()));

        mockMvc.perform(get("/api/incidents/abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("vecino_1022"));
    }

    // GET /api/incidents/{id} cuando NO existe responde 404.
    @Test
    @DisplayName("GET por id inexistente responde 404")
    void obtenerPorId_noExiste_responde404() throws Exception {
        when(incidentService.getIncidentById("noexiste")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/incidents/noexiste"))
                .andExpect(status().isNotFound());
    }

    // PUT /api/incidents/{id} responde 200 al actualizar.
    @Test
    @DisplayName("PUT actualiza incidente y responde 200")
    void actualizar_responde200() throws Exception {
        Incident validado = incidentEjemplo();
        validado.setStatus(IncidentStatus.VALIDATED);
        when(incidentService.updateIncident(anyString(), any(), any(), any()))
                .thenReturn(validado);

        mockMvc.perform(put("/api/incidents/abc123")
                        .param("latitude", "-36.0")
                        .param("longitude", "-73.0")
                        .param("status", "VALIDATED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("VALIDATED"));
    }

    // DELETE /api/incidents/{id} responde 204 No Content.
    @Test
    @DisplayName("DELETE elimina incidente y responde 204")
    void eliminar_responde204() throws Exception {
        doNothing().when(incidentService).deleteIncident("abc123");

        mockMvc.perform(delete("/api/incidents/abc123"))
                .andExpect(status().isNoContent());

        verify(incidentService, times(1)).deleteIncident("abc123");
    }
}