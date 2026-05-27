# 1.imagen liviana de Java 17 como base
FROM amazoncorretto:17-al2023-headless

# 2. creacion de la carpeta de trabajo dentro del contenedor
WORKDIR /app

# 3. se copia el archivo .jar que compilamos con Maven a la carpeta /app
COPY target/*.jar app.jar

# 4. Comando para ejecutar el microservicio cuando el contenedor encienda
ENTRYPOINT ["java", "-jar", "app.jar"]