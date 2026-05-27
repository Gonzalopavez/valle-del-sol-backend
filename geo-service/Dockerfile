# ETAPA 1: Compilación interna dentro de Docker
FROM maven:3.9-amazoncorretto-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Imagen final liviana de ejecución
FROM amazoncorretto:17-al2023-headless
WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]