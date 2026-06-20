FROM eclipse-temurin:17-jre-alpine as builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
COPY /config config

RUN mkdir agent && wget -O agent/dd-java-agent.jar https://dtdg.co/latest-java-tracer

FROM eclipse-temurin:17-jre-alpine
RUN mkdir ./app
COPY --from=builder application.jar ./app
COPY --from=builder agent/ ./app/agent
COPY --from=builder config/ ./app/config

WORKDIR /app

ENTRYPOINT ["java", "-jar", "-XX:+UseSerialGC", "application.jar"]