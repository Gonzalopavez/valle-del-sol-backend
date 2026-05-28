

# Geo-Service (Microservicio de Inteligencia Territorial) — Puerto: 8083


Componente backend del ecosistema **Valle del Sol** encargado de la  geolocalización y cálculo de perímetros de riesgo. 

Su función principal es procesar las coordenadas en vivo de los ciudadanos, contrastarlas mediante algoritmos matemáticos con los focos de emergencia validados, y determinar qué dispositivos se encuentran dentro del área de peligro para activar protocolos de evacuación.


## Tecnologías y Herramientas
* **Java 17** y **Spring Boot 3.9.15**
* **PostgreSQL**: Base de datos relacional (SQL) utilizada para la persistencia de las zonas geográficas, cuadrantes de la comuna y registros temporales de ubicación de usuarios.
* **RabbitMQ**: Bróker de mensajería asíncrona utilizado para consumir eventos del bus de integración en segundo plano.



## Patrones de Diseño Implementados en este Servicio

* **Repository Pattern (Patrón Repositorio):** Implementado mediante Spring Data JPA (`JpaRepository`) para gestionar las operaciones y consultas sobre la base de datos relacional PostgreSQL, aislando la infraestructura SQL de la lógica matemática.


* **Data Transfer Object (DTO):** Implementado a través de dos contratos de datos estrictos que no representan tablas físicas, sino mensajes en tránsito:

  1. `IncidentValidatedEvent`: Molde asíncrono para consumir e interpretar el JSON recibido desde RabbitMQ.
  2. `UserLocationRequest`: Molde síncrono para capturar las coordenadas HTTP enviadas por los dispositivos.




##  Endpoints Principales (API REST)

### 1. Simular / Guardar Ubicación Actual del Usuario (POST)
* **Ruta nativa:** `http://localhost:8083/api/v1/geo/location` (Vía BFF: `http://localhost:8080/api/bff/geo/location`)
* **Función:** Captura la posición geográfica en tiempo real enviada por el dispositivo móvil del ciudadano para evaluar su cercanía con zonas de riesgo.


* **Cuerpo de la petición (JSON) — *Mapeado en `UserLocationRequest`*:**
```json
{
  "userId": "user_Wacoldo_celular",
  "latitude": -36.7262,
  "longitude": -73.1156,
  "deviceId": "Iphone17-pro-max"
}