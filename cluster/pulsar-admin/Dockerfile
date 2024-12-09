FROM maven:3.8.1-openjdk-11-slim AS builder
WORKDIR /
COPY target/pulsar-admin-1.0-SNAPSHOT.jar admin.jar
CMD ["java", "-jar", "admin.jar"]
