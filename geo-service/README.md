# Geo-service (Microservicio de geo) Corriendo en el puerto 8083

Backend encargado de la captura de reportes , busqueda de coordenadas de usuarios cerca del fuego , comparacion de 
coordenadas de usuarios y coordenadas del reporte y calculo matematica para saber que usarios se encuentran cerca del area del reporte, en el proyecto 
**Valle del Sol**.

##  Tecnologías Utilizadas
* **Java 17** y **Spring Boot 3.5.15**
* **Apache Maven** (Gestor de dependencias) **3.9.15**
* **PostgreSQL** (Base de datos para guardar usuarios fijos pero TEMPORALES POR EL MOMENTO)
* **RabbitMQ** (Servidor de mensajería asíncrona para despacho de eventos)

##  Requisitos Previos
* Tener **Docker Desktop** encendido.
* Instancia de **postgreSQL** corriendo en el puerto por defecto `5432`.




Endpoints Principales (API REST):

POST http://localhost:8083/api/v1/geo/location : ESTO PARA GUARDAR Y SIMULAR LA UBICACION ACTUAL DE USUARIOS


{
  "userId": "user_XXX_lugar",
  "latitude": -36.7262,
  "longitude": -73.1156,
  "deviceId": "Samsung-S24"
}





##  Instalación y Orden de ejecucion 

### Paso 1: Levantar la mensajería con Docker
Abrir una terminal en en el PC (en cualquier ubicación) y encender el contenedor oficial de RabbitMQ:

docker start rabbitmq

### Paso 2: Navegar a la carepta especifica del servicio 

cd geo-service


### Paso 3: Limpiar y Compilar código (ESPERAR MENSAJE DE BUILD SUCCESS)

./mvnw clean compile

### Paso 4: Levantar el Microservicio (El microservicio se quedará escuchando de forma activa a RabbitMQ.)

./mvnw spring-boot:run

