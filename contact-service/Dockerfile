FROM eclipse-temurin:17-jre-alpine

RUN mkdir /app
COPY build/libs/de.rlang.contact-service-all.jar /app/application.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "application.jar"]