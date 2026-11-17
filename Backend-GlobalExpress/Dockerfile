# ---------- 1) Compilar el proyecto con Maven ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Se copian primero los archivos de Maven para aprovechar la caché de dependencias
COPY .mvn .mvn
COPY mvnw pom.xml ./
# Por si mvnw se guardó con finales de línea de Windows (CRLF)
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
RUN ./mvnw -B -q dependency:go-offline

COPY src src
RUN ./mvnw -B -q package -DskipTests

# ---------- 2) Imagen final, más liviana: solo Java + el .jar ----------
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Hora de Perú: las ventas y los reportes usan la fecha del servidor
ENV TZ=America/Lima
EXPOSE 8080

# Render gratuito tiene 512 MB de memoria: se limita el uso de la JVM y se usa el recolector más liviano
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=65", "-XX:+UseSerialGC", "-Duser.timezone=America/Lima", "-jar", "app.jar"]
