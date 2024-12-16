FROM openjdk:22-jdk-slim AS builder
WORKDIR /
COPY target/converter-1.0.jar converter.jar
CMD ["java", "-jar", "converter.jar"]
