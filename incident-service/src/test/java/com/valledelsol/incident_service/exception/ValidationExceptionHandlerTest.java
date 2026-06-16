package com.valledelsol.incident_service.exception;

import com.valledelsol.incident_service.model.Incident;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Prueba unitaria del ValidationExceptionHandler.
 * Simulamos un error de validacion y verificamos que el handler
 * devuelva un 400 (Bad Request) con el mensaje del campo invalido.
 */
@DisplayName("Pruebas del ValidationExceptionHandler")
class ValidationExceptionHandlerTest {

    private final ValidationExceptionHandler handler = new ValidationExceptionHandler();

    @Test
    @DisplayName("Un error de validacion devuelve 400 con el campo y su mensaje")
    void errorValidacion_devuelve400() {
        // Arrange: usamos un Incident real para que el campo "description" exista.
        BindingResult bindingResult =
                new BeanPropertyBindingResult(new Incident(), "incident");
        bindingResult.rejectValue("description", "error",
                "La descripcion es obligatoria");

        MethodParameter parameter = mock(MethodParameter.class);
        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(parameter, bindingResult);

        // Act
        ResponseEntity<Map<String, String>> respuesta =
                handler.handleValidationErrors(ex);

        // Assert
        Map<String, String> cuerpo = respuesta.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNotNull(cuerpo);
        assertEquals("La descripcion es obligatoria", cuerpo.get("description"));
    }

    @Test
    @DisplayName("Varios errores se devuelven todos en el mapa")
    void variosErrores_devuelveTodos() {
        // Arrange: dos campos invalidos sobre un Incident real.
        BindingResult bindingResult =
                new BeanPropertyBindingResult(new Incident(), "incident");
        bindingResult.rejectValue("description", "error", "Descripcion invalida");
        bindingResult.rejectValue("latitude", "error", "Latitud invalida");

        MethodParameter parameter = mock(MethodParameter.class);
        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(parameter, bindingResult);

        // Act
        ResponseEntity<Map<String, String>> respuesta =
                handler.handleValidationErrors(ex);

        // Assert
        Map<String, String> cuerpo = respuesta.getBody();
        assertNotNull(cuerpo);
        assertEquals(2, cuerpo.size());
        assertTrue(cuerpo.containsKey("description"));
        assertTrue(cuerpo.containsKey("latitude"));
    }
}