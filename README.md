# ☀️ Sistema de Gestión de Emergencias - Valle del Sol (Backend) 
# Patrón Arquitectónico: Microservicios + BFF (Backend For Frontend)

Este repositorio contiene la arquitectura de software del ecosistema backend para el proyecto **Valle del Sol**. La solución está diseñada bajo un enfoque de **Microservicios** distribuidos, utilizando un patrón **BFF (Backend For Frontend)** como puerta de entrada única y (en el puerto 8080) comunicación asíncrona basada en eventos.

Este diseño cumple una función crítica de **seguridad y optimización**: el frontend jamás interactúa directamente con los servicios del negocio. El BFF actúa como un escudo que centraliza la autenticación, mitiga ataques externos y orquesta las peticiones de forma síncrona hacia los microservicios internos (`incident-service` y `geo-service`) mediante el uso de clientes reactivos (`WebClient`).


## IMPLEMENTACION DE DOCKER

Toda la infraestructura se encuentra completamente **contenedorizada** y automatizada mediante **Docker**, garantizando portabilidad absoluta, desacoplamiento de entornos locales y persistencia políglota.











## Patrones de Diseño Implementados

Para dar cumplimiento a los estándares de desarrollo de software y asegurar la mantenibilidad y escalabilidad del ecosistema backend, se implementaron tres patrones de diseño fundamentales en el código fuente:

1. **Data Transfer Object (DTO):** Utilizado transversalmente en todos los servicios (ej. `IncidentDTO`, `IncidentValidatedEvent`, `UserLocationRequest`) para desacoplar completamente la capa de presentación de la capa de datos. Al no exponer las entidades de la base de datos hacia el exterior, se resguarda el contrato con el cliente y se previene la inyección maliciosa de atributos de persistencia.


2. **Repository Pattern (Patrón Repositorio):** Implementado mediante abstracciones de Spring Data (`MongoRepository` y `JpaRepository`). Este patrón encapsula y aísla por completo la lógica de acceso a datos (tanto NoSQL como Relacional) de la lógica de negocio. Esto permite que las reglas del negocio sean independientes .


3. **Publish-Subscribe / Observer (Event-Driven Architecture):** Implementado mediante la integración nativa con **RabbitMQ**. Cuando un reporte es modificado al estado `VALIDATED` en el `incident-service`, se despacha de forma inmediata un evento asíncronono al bróker de mensajería. Esto elimina el acoplamiento rígido, permitiendo que el hilo de ejecución responda de inmediato al cliente, mientras el `geo-service` reacciona y procesa el impacto territorial en segundo plano (asíncronamente) mediante métodos `@RabbitListener`.

---

## Arquitectura del Sistema e Infraestructura

La solución se compone de tres microservicios desarrollados en **Java 17 (Amazon Corretto)** con **Spring Boot 3**, integrados en una red virtual privada y aislada de Docker (`valledelsol-network`) utilizando un driver tipo *bridge*:





1. **`bff-service` (Puerto `8080`):** Punto de entrada único de la arquitectura (API Gateway / Backend For Frontend). Orquesta y redirige las peticiones del cliente hacia los servicios internos mediante comunicación reactiva con `WebClient`.


2. **`incident-service` (Puerto `8081`):** Microservicio encargado del ciclo de vida y lógicas de negocio de los incidentes.


3. **`geo-service` (Puerto `8083`):** Microservicio responsable de las capacidades geográficas, cálculos de distancias y gestión territorial.


### Persistencia Políglota y Eventos (Infraestructura)

* **MongoDB (Puerto `27017`):** Base de datos NoSQL dedicada al almacenamiento de incidentes de estructura dinámica (`incidentdb`).

* **PostgreSQL (Puerto `5432`):** Base de datos relacional robusta dedicada a la consistencia geográfica (`geo_db`).

* **RabbitMQ (Puertos `5672` / `15672`):** Broker de mensajería AMQP que gestiona la comunicación asíncrona orientada a eventos entre microservicios (intercambio y consumo de eventos topográficos).

---

##  Requisitos Previos Obligatorios

Para compilar y desplegar la arquitectura completa de forma local, solo se requiere contar con las siguientes herramientas instaladas en la máquina anfitriona:

* **Java Development Kit (JDK) 17**
* **Apache Maven 3.9.15**
* **Docker Desktop** (Asegurarse de que esté activo y corriendo)


>  **Nota de Infraestructura:** No se requiere la instalación manual ni residente de PostgreSQL, MongoDB o RabbitMQ en el sistema operativo local, ya que estos motores se inicializan automáticamente dentro de la capa de virtualización de Docker.

---

## Despliegue Rápido (Local)

### : Orquestación y Construcción de Contenedores
Ejecute Docker Compose para construir las imágenes personalizadas basadas en el ecosistema virtualizado y encender toda la arquitectura de datos y servicios en una sola red:

docker-compose up --build

Una vez que finalicen los registros de inicialización en la terminal, los contextos de Spring Boot estarán sincronizados y en estado de escucha (Listening)
