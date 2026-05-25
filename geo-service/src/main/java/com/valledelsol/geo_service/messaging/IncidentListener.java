package com.valledelsol.geo_service.messaging;


import com.valledelsol.geo_service.config.RabbitMQConfig;
import com.valledelsol.geo_service.dto.IncidentValidatedEvent;
import com.valledelsol.geo_service.model.UserLocation;
import com.valledelsol.geo_service.service.LocationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class IncidentListener {

    private final LocationService locationService;

    // Inyectamos el servicio de localización
    public IncidentListener(LocationService locationService) {
        this.locationService = locationService;
    }

    @RabbitListener(queues = RabbitMQConfig.INCIDENT_QUEUE)
    public void handleIncidentValidated(IncidentValidatedEvent event) {
        System.out.println("\n=== [RabbitMQ] EVENTO RECIBIDO DESDE EL SERVICIO DE INCIDENTES ===");
        System.out.println("Incidente ID: " + event.getId());
        System.out.println("Coordenadas Incidente: Lat: " + event.getLatitude() + " | Lng: " + event.getLongitude());
        
        //Definimos un radio de búsqueda (ejemplo: 1.0 kilómetro a la redonda)
        double radioBusquedaKm = 1.0; 
        
        System.out.println("Buscando usuarios a menos de " + radioBusquedaKm + " km en PostgreSQL...");
        
        // Consultamos la lista real de usuarios en PostgreSQL que están cerca
        List<UserLocation> usuariosCercanos = locationService.buscarUsuariosCercanos(
                event.getLatitude(), 
                event.getLongitude(), 
                radioBusquedaKm
        );
        
        // Mostramos el resultado en consola
        if (usuariosCercanos.isEmpty()) {
            System.out.println("❌ No se encontraron usuarios en el radio de peligro.");
        } else {
            System.out.println("🚨 ¡ALERTA! Se encontraron " + usuariosCercanos.size() + " usuarios cerca:");
            for (UserLocation user : usuariosCercanos) {
                System.out.println("   -> Notificar a Usuario ID: " + user.getUserId() + " (Dispositivo: " + user.getDeviceId() + ")");
            }
        }
        System.out.println("===============================================================\n");
    }
}