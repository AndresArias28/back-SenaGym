# Imagen base oficial de Java
FROM eclipse-temurin:17-jdk

# Crea un directorio dentro del contenedor
WORKDIR /app

# Copia el archivo .jar compilado
COPY target/*.jar app.jar

# Expón el puerto en el que corre tu app (ajústalo si usas otro)
EXPOSE 6090

# Comando para ejecutar el jar
ENTRYPOINT ["java", "-jar", "app.jar"]
