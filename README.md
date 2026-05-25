
## Comunicacion: corriendo en el puerto 5672, Es el puerto interno que usa springBoot en el codigo para mandar mensajes a RabbitMQ

### PARA LA CORRECTA EJECUCCION DE TODO EL PROYECTO SE REQUIERE TENER LOS SIGUIENTES REQUERIMIENTOS IMPORTANTES:

1. dockerDesktop instalado y corriendo

2. Levantar RabbitMQ con docker

  **2.1** Abrir terminal gitbash y ejecutar: docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
      Este comando descarga y levanta RabbitMQ en segundo plano
  **2.2** Puerto 15672 se encuentra **La consola web visual de RABBITMQ**  http://localhost:15672/ 
      (si pide contraseña y usuario, ambos son guest)

3. MongoDB Compass instalado y corriendo (MongoDB corre en el puerto 27017)

4. Postgresql instalado y corriendo (Postresql corre en el puerto 5432 )

5. java 17 INSTALADO

6. Maven 3.9.15 INSTALADO

7. SEGUIR CON EL README.md de Incident-service, luego bff-service y finalmente geo-service



