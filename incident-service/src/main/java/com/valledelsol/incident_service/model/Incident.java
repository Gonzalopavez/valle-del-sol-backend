package com.valledelsol.incident_service.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
// Representa: fecha y hora del reporte
import java.time.LocalDateTime;



@Data //genera getters, setters, toString, equals y hashCode automáticamente
@Builder//genera un builder para crear objetos de forma fluida
@NoArgsConstructor
@AllArgsConstructor


// Modelo de incidente
// Este objeto se guardará en la base de datos MongoDB y se usará para transferir datos entre el controlador y el servicio
//la coleccion se llamrá "incidents" en MongoDB
@Document(collection = "incidents")


public class Incident {

    @Id
    private String id;

    private String userId;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 100, message = "La descripcion no puede superar los 100 caracteres")
    private String description;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "Latitud invalida")
    @DecimalMax(value = "90.0", message = "Latitud invalida")
    private Double latitude;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "Longitud invalida")
    @DecimalMax(value = "180.0", message = "Longitud invalida")
    private Double longitude;

    private String imageUrl;

    private IncidentStatus status;

    private LocalDateTime createdAt;
}