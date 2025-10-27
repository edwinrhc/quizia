
# Imagen con java 21
FROM openjdk:21-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el jar compilado dentro del contenedor
COPY target/quiz-ai-backend-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java","jar","app.jar"]

