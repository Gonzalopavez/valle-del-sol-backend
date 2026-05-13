package com.valledelsol.incident_service.model;


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
    private String id; // ID único generado por MongoDB

    private String userId;

    private String description;

    private Double latitude;

    private Double longitude;

    private String imageUrl;

    private IncidentStatus status;

    private LocalDateTime createdAt;

}