# Incident Service (Microservicio de Incidentes) — Puerto: 8081



Componente backend del ecosistema **Valle del Sol** encargado de la captura, persistencia, gestión y ciclo de vida de los reportes de emergencias ciudadanas. 



Su función principal es procesar las solicitudes de incidentes y, bajo reglas de negocio específicas, notificar de forma reactiva a otros componentes del sistema mediante eventos.

## Tecnologías y Herramientas
* **Java 17** y **Spring Boot 3.9.15**
* **MongoDB**: Base de datos NoSQL utilizada para el almacenamiento flexible de los documentos de incidentes en formato JSON.
* **RabbitMQ**: Bróker de mensajería asíncrona utilizado para el despacho de eventos hacia el bus de integración.


##  Patrones de Diseño Implementados en este Servicio

* **Repository Pattern (Patrón Repositorio):** Implementado mediante la interfaz `IncidentRepository` que extiende de `MongoRepository`. Aísla la lógica de acceso a datos NoSQL de las reglas de negocio de la aplicación.


* **Data Transfer Object (DTO):** Utilizado para transferir la información de las alertas de manera limpia, desacoplando los modelos de persistencia de MongoDB de los contratos de exposición de la API.


* **Publish-Subscribe (Event-Driven):** Integrado mediante `RabbitTemplate`. Cuando un incidente es actualizado al estado `VALIDATED`, el servicio publica un evento asíncrono hacia el Exchange de RabbitMQ, permitiendo que otros servicios reaccionen en segundo plano sin bloquear el flujo principal de la petición.



## Endpoints Principales (API REST)

> 💡 **Nota de Arquitectura:** Aunque este microservicio expone nativamente sus endpoints en el puerto `8081`, el acceso regular de producción se canaliza centralizado a través del **BFF en el puerto 8080**.

### 1. Registrar una emergencia (POST)
* **Ruta:** `http://localhost:8081/api/incidents` (Vía BFF: `http://localhost:8080/api/bff/incidents`)
* **Estado inicial automático:** `PENDING`
* **Cuerpo de la petición (JSON):**
```json
{
  "userId": "vecino_1022",
  "description": "Fuego en unimarc",
  "latitude": -36.8269,
  "longitude": -73.0498,
  "imageUrl": "[https://valle-del-sol-s3.amazonaws.com/fotos/incendio_01.jpg](https://valle-del-sol-s3.amazonaws.com/fotos/incendio_01.jpg)"
}