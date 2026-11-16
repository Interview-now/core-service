# Stage 1: Build the application using Maven & JDK 21
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Pehle pom.xml copy karke dependencies cache karein
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Source code copy karke JAR build karein
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Lightweight JRE 21 runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Build stage se generated JAR copy karein
COPY --from=build /app/target/*.jar app.jar

# Default port expose karein (dev profile ke hisab se 4000)
EXPOSE 4000

# Active profile environment variable se override kiya ja sakta hai
ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java", "-jar", "app.jar"]