FROM maven:3.8.1-openjdk-11-slim AS builder
WORKDIR /
COPY target/converter-1.0-SNAPSHOT.jar converter.jar
CMD ["java", "-jar", "converter.jar", "localhost:6650"]
