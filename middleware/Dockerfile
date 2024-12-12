FROM openjdk:22-jdk-slim AS builder
WORKDIR /
COPY load-handler/target/load-handler-1.0.jar load-handler.jar
COPY pulsar-functions/conversion-output-handler/target/conversion-output-handler-1.0.jar conversion-output-handler.jar
CMD ["java", "-jar", "load-handler.jar", "8080", "localhost:8080"]
