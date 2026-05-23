




# BFF Service (Backend For Frontend)

Componente que actúa como **orquestador y puerta de entrada única** para las aplicaciones del Frontend. Su función principal es centralizar las peticiones del cliente, aislar la topología de la red interna y redirigir los flujos hacia los microservicios correspondientes de manera eficiente y asíncrona.

##  Tecnologías Utilizadas
* **Java 17** y **Spring Boot 3.5.15**
* **Spring WebFlux (`WebClient`)**: Implementado para realizar llamadas HTTP no bloqueantes y de alto rendimiento hacia la red interna de microservicios.
* **Lombok**: Para la optimización de código limpio (DTOs sin boilerplate).



##  Instalación y Orden de ejecucion ( SEGUNDA FASE DESPUES DE TENER EL INCIDENT_SERVICE CORRIENDO )

### Paso 1: Confirmar servicios internos

Asegúrarse de haber completado la guía de arranque del `incident-service` y que este responda en el puerto `8081` y además
tener corriendo el contender de RABBITMQ



### Paso 2: Navegar a la carpeta del BFF

cd bff-service


### Paso 3: Limpiar y Compilar código (ESPERAR MENSAJE DE BUILD SUCCESS)

./mvnw clean compile

### Paso 4: Levantar el Orquestador (bff) (El microservicio se quedará escuchando de forma activa en el puerto 8081. NO CERRAR LA TERMINAL PARA MANTENER EL SERVICIO VIVO.)

./mvnw spring-boot:run
