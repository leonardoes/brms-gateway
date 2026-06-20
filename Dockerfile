FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests && \
    cp target/*.jar application.jar

FROM eclipse-temurin:17-jre-alpine
RUN mkdir ./app
COPY --from=builder /build/application.jar ./app/
COPY config ./app/config

WORKDIR /app

ENTRYPOINT ["java", "-jar", "-XX:+UseSerialGC", "application.jar"]