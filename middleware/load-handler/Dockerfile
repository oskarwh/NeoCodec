FROM maven:3.8.1-openjdk-11-slim AS builder
WORKDIR /
COPY target/load-handler-1.0.jar load-handler-1.0.jar
CMD ["java", "-jar", "load-handler-1.0.jar", "8080", "localhost:8080"]
