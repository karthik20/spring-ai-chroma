# Multi-stage Dockerfile for Spring AI ChromaDB Integration

# Stage 1: Python base for ChromaDB server
FROM python:3.11-slim as chroma-base

# Install ChromaDB dependencies
RUN pip install --no-cache-dir chromadb==0.4.22

# Create directory for ChromaDB data persistence
RUN mkdir -p /chroma/data

# Stage 2: Java build stage
FROM openjdk:17-jdk-slim as java-build

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 3: Runtime stage with both Python and Java
FROM openjdk:17-jdk-slim as runtime

# Install Python and pip
RUN apt-get update && \
    apt-get install -y python3 python3-pip curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create symbolic link for python command
RUN ln -s /usr/bin/python3 /usr/bin/python

# Install ChromaDB
RUN pip3 install --no-cache-dir chromadb==0.4.22

# Create application user
RUN useradd -r -s /bin/false appuser

# Create directories
RUN mkdir -p /app /chroma/data && chown -R appuser:appuser /app /chroma

# Copy the built JAR from build stage
COPY --from=java-build /app/target/*.jar /app/spring-ai-chroma.jar

# Copy startup script
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh && chown appuser:appuser /app/docker-entrypoint.sh

# Switch to application user
USER appuser

WORKDIR /app

# Expose ports
EXPOSE 8080 8000

# Use the startup script
ENTRYPOINT ["/app/docker-entrypoint.sh"]