# Dockerfile for Spring Boot app with ONNX model downloads

FROM openjdk:21-jdk-slim

WORKDIR /app

# Install Maven, curl, and git (needed for huggingface-cli)
RUN apt-get update && \
    apt-get install -y maven curl git python3 python3-pip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create directories for ONNX models
RUN mkdir -p /app/models/onnx/all-MiniLM-L6-v2

# Download ONNX model and tokenizer directly using curl
# Note: Using direct download URLs instead of git-lfs for simplicity
RUN curl -L https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2/resolve/main/onnx/model.onnx \
    --output /app/models/onnx/all-MiniLM-L6-v2/model.onnx && \
    curl -L https://huggingface.co/sentence-transformers/all-MiniLM-L6-v2/resolve/main/tokenizer.json \
    --output /app/models/onnx/all-MiniLM-L6-v2/tokenizer.json

# Copy project files
COPY pom.xml ./
COPY src ./src

# Build the application
RUN mvn clean install -DskipTests

# Expose Spring Boot port
EXPOSE 8080

# Set environment variables for the model paths - using file:/ prefix for filesystem paths
ENV ONNX_MODEL_PATH=file:/app/models/onnx/all-MiniLM-L6-v2/model.onnx
ENV ONNX_TOKENIZER_PATH=file:/app/models/onnx/all-MiniLM-L6-v2/tokenizer.json

# Create directory for cache
RUN mkdir -p /app/cache && chmod 777 /app/cache

# For debugging purposes, you can uncomment the following line to keep the container running
# CMD ["sleep", "infinity"]

# Run the Spring Boot application with model path environment variables and API-only mode
ENTRYPOINT ["java", "-Dapp.interactive-mode=false", "-jar", "target/springai-chromadb-0.0.1-SNAPSHOT.jar"]