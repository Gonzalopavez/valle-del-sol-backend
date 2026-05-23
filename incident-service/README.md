

# Incident Service (Microservicio de Incidentes)

Backend encargado de la captura, persistencia y ciclo de vida de los reportes de emergencias ciudadanas en el proyecto 
**Valle del Sol**.

##  Tecnologías Utilizadas
* **Java 17** y **Spring Boot 3.5.15**
* **Apache Maven** (Gestor de dependencias)
* **MongoDB** (Base de datos NoSQL para almacenamiento flexible de documentos JSON)
* **RabbitMQ** (Servidor de mensajería asíncrona para despacho de eventos)

##  Requisitos Previos
* Tener **Docker Desktop** encendido.
* Instancia de **MongoDB** corriendo en el puerto por defecto `27017`.




Endpoints Principales (API REST):

POST http://localhost:8081/api/incidents : Registra un nuevo incidente enviado por un vecino (Estado inicial: PENDING).

GET  http://localhost:8081/api/incidents : Retorna el listado completo de incidentes guardados en MongoDB.

GET2 http://localhost:8081/api/incidents/ID : Retorna un reporte por su ID

PUT  http://localhost:8081/api/incidents/ID_DEL_REPORTE?latitude=-11.1111&longitude=-22.2222&status=VALIDATED : Permite al Administrador Municipal corregir coordenadas y actualizar el estado a VALIDATED.


EN EL PUT DE ARRIBA PUEDES MODIFICAR (ACTULIZAR) LO SIGUIENTE:

ID_DEL_REPORTE

latitude=

longitude=

status=



IMPORTANTE: Al cambiar a VALIDATED, este endpoint despacha automáticamente un evento JSON a RabbitMQ.



##  Instalación y Orden de ejecucion ( PRIMERA FASE ANTES DE IR AL BFF )

### Paso 1: Levantar la mensajería con Docker
Abrr una terminal en en el PC (en cualquier ubicación) y encender el contenedor oficial de RabbitMQ:

docker start rabbitmq

### Paso 2: Navegar a la carepta especifica del servicio 

cd incident-service


### Paso 3: Limpiar y Compilar código (ESPERAR MENSAJE DE BUILD SUCCESS)

./mvnw clean compile

### Paso 4: Levantar el Microservicio (El microservicio se quedará escuchando de forma activa en el puerto 8081. NO CERRAR LA TERMINAL PARA MANTENER EL SERVICIO VIVO.)

./mvnw spring-boot:run

