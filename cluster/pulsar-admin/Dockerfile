FROM maven:3.8.1-openjdk-11-slim AS builder
WORKDIR /
COPY target/pulsar-admin-1.0.jar admin.jar
CMD ["java", "-jar", "admin.jar", "8080", "localhost:8080"]
