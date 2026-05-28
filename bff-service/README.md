# BFF Service (Backend For Frontend) — Puerto: 8080

Este componente actúa como el **orquestador y la puerta de entrada única (API Gateway)** para las aplicaciones del Frontend dentro del ecosistema Valle del Sol. 



Su función principal en la arquitectura es centralizar las peticiones del cliente y redirigir los flujos HTTP de manera eficiente hacia los microservicios correspondientes del backend.


##  Tecnologías y Herramientas
* **Java 17** y **Spring Boot 3.9.15**
* **Spring WebFlux (`WebClient`)**: Implementado para realizar llamadas HTTP reactivas, no bloqueantes y de alto rendimiento hacia la red interna de microservicios.
* **Lombok**: Para la optimización de código limpio y eliminación de boilerplate en la capa de transferencia de datos.

##  Patrones de Diseño Implementados en este Servicio

* **Backend For Frontend (BFF) / API Gateway:** Funciona como un proxy seguro. El frontend solo le habla a este componente en el puerto `8080`, ocultando por completo que los microservicios internos corren en los puertos `8081` y `8083`. Centraliza el punto de contacto y protege la red interna.


* **Data Transfer Object (DTO):** Implementado en la capa de controladores para moldear y limpiar los JSON de respuesta, asegurando que el frontend reciba estrictamente los datos optimizados que necesita para renderizar la interfaz.

