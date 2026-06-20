FROM eclipse-temurin:17-jre-alpine as builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
COPY /config config

FROM eclipse-temurin:17-jre-alpine
RUN mkdir ./app
COPY --from=builder application.jar ./app
COPY --from=builder config/ ./app/config

WORKDIR /app

ENTRYPOINT ["java", "-jar", "-XX:+UseSerialGC", "application.jar"]