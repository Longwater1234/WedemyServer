# syntax=docker/dockerfile:1
# Build and Run docker image with --tag e.g. "wedemyserver"
FROM maven:3.8.6-eclipse-temurin-11-alpine AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn clean -DskipTests package


FROM eclipse-temurin:11-jre-alpine
WORKDIR /app
COPY --from=build /app/target/wedemyserver.jar /app
EXPOSE 9000
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "wedemyserver.jar"]