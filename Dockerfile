#  Imagen base optimizada con Java 21
FROM eclipse-temurin:21-jdk-jammy AS build

#  Directorio de trabajo
WORKDIR /app

# Copiar el jar compilado dentro del contenedor
COPY target/quizia-0.0.1-SNAPSHOT.jar app.jar

#Exponer el puerto (Render usará el valor de $PORT)
EXPOSE 8080

#Definir variables de entorno (opcional pero recomendable)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

#Comando de arranque
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
