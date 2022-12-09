# syntax=docker/dockerfile:1
FROM eclipse-temurin:11-jdk-alpine AS build
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
RUN ./mvnw clean DskipTests package
EXPOSE 9000
CMD ["java", "-jar", "target/wedemyserver-0.0.1-SNAPSHOT.jar"]

