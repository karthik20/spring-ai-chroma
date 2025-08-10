# Spring AI ChromaDB Integration

A Spring Boot application that integrates ChromaDB with Spring AI for semantic search capabilities, offering both REST API endpoints and an interactive command-line interface.

## Features

- Dual-mode operation:
  - REST API endpoints for application integration
  - Interactive CLI mode for direct semantic search queries
- ChromaDB integration for vector storage
- ONNX-based local embedding model (all-MiniLM-L6-v2)
- Docker and Docker Compose support with automated model downloads
- Persistent vector data storage

## Prerequisites

- Java 21 or later
- Maven 3.6 or later
- Docker and Docker Compose (for containerized deployment)

## Quick Start

### Using Docker Compose (Recommended)

1. Clone the repository:
```bash
git clone https://github.com/karthik20/spring-ai-chroma.git
cd spring-ai-chroma
```

2. Start the application:
```bash
docker-compose up --build
```

The application will be available at:
- Spring Boot API: `http://localhost:8080`
- ChromaDB: `http://localhost:8000`

### Manual Setup

1. Start ChromaDB server:
```bash
docker run -p 8000:8000 chromadb/chroma:latest
```

2. Build and run the Spring Boot application in API mode:
```bash
./mvnw clean package
java -Dapp.interactive-mode=false -jar target/springai-chromadb-0.0.1-SNAPSHOT.jar
```

3. For interactive CLI mode:
```bash
# Use the provided script
./run-interactive.sh

# Or run directly
./mvnw spring-boot:run -Dspring-boot.run.arguments="--app.interactive-mode=true"
```

## Application Modes

### API Mode

The default mode when running in Docker. Exposes REST endpoints for semantic search.

### Interactive CLI Mode

A command-line interface for interactive semantic searches:

```
Enter your search query: investment strategies for retirement
Document(id=6, text=Retirement Planning - Tips for planning your retirement effectively., metadata={category=retirement}, embedding=null, score=0.8579)
Document(id=3, text=Investment Strategies - Explore various investment strategies., metadata={category=investments}, embedding=null, score=0.8312)
Document(id=28, text=Investment Risk - Understanding investment risks., metadata={category=investment risk}, embedding=null, score=0.7854)
Performed similarity search in 127 ms
```

## API Endpoints

### Search
```bash
GET /api/search?query=investment+strategies
```

**Response:**
```json
[
  {
    "id": "3",
    "content": "Investment Strategies - Explore various investment strategies.",
    "score": 0.9214,
    "metadata": {
      "category": "investments"
    }
  },
  {
    "id": "28",
    "content": "Investment Risk - Understanding investment risks.",
    "score": 0.7932,
    "metadata": {
      "category": "investment risk"
    }
  }
]
```

## Configuration

### Application Configuration

Key configuration options in `application.yml`:

```yaml
# Application mode
app:
  interactive-mode: true  # true for CLI mode, false for API-only mode

# ChromaDB configuration
spring:
  ai:
    vectorstore:
      chroma:
        client:
          host: ${CHROMA_HOST:localhost}
          port: ${CHROMA_PORT:8000}
```

### Environment Variables

- `APP_INTERACTIVE_MODE`: Set to `false` for API-only mode (default in Docker)
- `CHROMA_HOST`: ChromaDB host (default: localhost)
- `CHROMA_PORT`: ChromaDB port (default: 8000)
- `ONNX_MODEL_PATH`: Path to ONNX model file
- `ONNX_TOKENIZER_PATH`: Path to tokenizer file

## ONNX Model Integration

This project uses the `all-MiniLM-L6-v2` sentence transformer model in ONNX format for generating embeddings locally:

- The Docker build automatically downloads the model from Hugging Face
- Model files are stored in `/app/models/onnx/all-MiniLM-L6-v2/` in the container
- The application uses a file-based cache directory to improve embedding generation performance

## Docker Setup

### Dockerfile

The Dockerfile:
1. Uses OpenJDK 21 as the base image
2. Installs necessary dependencies
3. Downloads the ONNX model files
4. Builds the application
5. Configures API-only mode for container operation

### Docker Compose

The docker-compose.yml sets up:
1. ChromaDB service with appropriate port mappings
2. Spring Boot application with ChromaDB connection
3. Volume mounts for persistent data

## Development

### Running Tests

```bash
./mvnw test
```

### Building the Project

```bash
./mvnw clean package
```

### Directory Structure

```
spring-ai-chroma/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── karthik/
│   │   │           └── springaichroma/
│   │   │               ├── controller/
│   │   │               ├── model/
│   │   │               ├── service/
│   │   │               └── SpringAiChromaApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── docker-compose.yml
├── Dockerfile
└── run-interactive.sh
```

## Architecture

- **Dual Interface**: API and CLI interfaces using the same underlying services
- **Spring AI Integration**: Leverages Spring AI for vector store operations
- **ONNX Runtime**: Local embedding generation without external API dependencies
- **ChromaDB**: Vector database for storing and retrieving embeddings

## Technologies Used

- Spring Boot 3.5.x
- Java 21
- Spring AI
- ChromaDB
- ONNX Runtime
- Docker & Docker Compose

## License

This project is licensed under the MIT License.
