# ETAPA 1: Compilación interna dentro de Docker
FROM maven:3.9-amazoncorretto-17 AS builder
WORKDIR /build
# Copiar el archivo de configuración de Maven y el código fuente
COPY pom.xml .
COPY src ./src
# Compilar y empaquetar el código fuente saltando los tests
RUN mvn clean package -DskipTests

# ETAPA 2: Imagen final liviana de ejecución
FROM amazoncorretto:17-al2023-headless
WORKDIR /app
# Tomar el archivo .jar generado en la ETAPA 1
COPY --from=builder /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]