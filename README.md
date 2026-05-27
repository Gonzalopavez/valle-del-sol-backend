# ☀️ Sistema de Gestión de Emergencias - Valle del Sol (Backend)

Este repositorio contiene la arquitectura de software del ecosistema backend para el proyecto **Valle del Sol**. La solución está diseñada bajo un enfoque de **Microservicios** distribuidos, utilizando un patrón **BFF (Backend For Frontend)** como puerta de entrada única y comunicación asíncrona basada en eventos.

Toda la infraestructura se encuentra completamente **contenedorizada** y automatizada mediante **Docker**, garantizando portabilidad absoluta, desacoplamiento de entornos locales y persistencia políglota.

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

##  Guía de Despliegue Rápido (Local)

Siga estrictamente los siguientes pasos en su terminal de comandos para compilar y levantar el ecosistema completo desde la raíz del proyecto:

### Paso 1: Compilación de Binarios (Maven Artifacts)
Genere los empaquetados `.jar` actualizados de los tres microservicios ejecutando el siguiente comando en la raíz madre:

# Compilar todo el proyecto omitiendo las pruebas unitarias
mvn clean package -DskipTests

### Paso 2: Orquestación y Construcción de Contenedores
Ejecute Docker Compose para construir las imágenes personalizadas basadas en el ecosistema virtualizado y encender toda la arquitectura de datos y servicios en una sola red:

docker-compose up --build

Una vez que finalicen los registros de inicialización en la terminal, los contextos de Spring Boot estarán sincronizados y en estado de escucha (Listening)
