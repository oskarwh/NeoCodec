FROM openjdk:22-jdk-slim AS builder
WORKDIR /

RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY target/converter-1.0.jar converter.jar
CMD ["java", "-jar", "converter.jar"]
