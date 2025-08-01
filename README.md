# Spring AI ChromaDB Integration

A Spring Boot application that integrates with ChromaDB to provide semantic search capabilities using OpenAI embeddings.

## Features

- REST API endpoints for semantic search
- ChromaDB integration for vector storage
- OpenAI embeddings for text vectorization
- Multi-stage Docker build with Python and Java
- Persistent data storage with Docker volumes

## Prerequisites

- Java 17 or later
- Maven 3.6 or later
- Docker and Docker Compose
- OpenAI API key

## Quick Start

### Using Docker Compose (Recommended)

1. Clone the repository:
```bash
git clone <repository-url>
cd spring-ai-chroma
```

2. Set your OpenAI API key:
```bash
export OPENAI_API_KEY=your-openai-api-key-here
```

3. Start the application:
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

2. Build and run the Spring Boot application:
```bash
./mvnw clean package
export OPENAI_API_KEY=your-openai-api-key-here
java -jar target/spring-ai-chroma-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Health Check
```bash
GET /api/search/health
```

### Add Documents
```bash
POST /api/search/documents
Content-Type: application/json

[
  {
    "content": "Your document content here",
    "metadata": {
      "title": "Document Title",
      "category": "example"
    }
  }
]
```

### Search Similar Documents (POST)
```bash
POST /api/search/similarity
Content-Type: application/json

{
  "query": "your search query",
  "limit": 5
}
```

### Search Similar Documents (GET)
```bash
GET /api/search/similarity?q=your+search+query&limit=5
```

## Example Usage

1. Add some sample documents:
```bash
curl -X POST http://localhost:8080/api/search/documents \
  -H "Content-Type: application/json" \
  -d '[
    {
      "content": "Spring Boot is a Java framework that makes it easy to create stand-alone applications.",
      "metadata": {"category": "technology", "topic": "java"}
    },
    {
      "content": "ChromaDB is an open-source embedding database built for AI applications.",
      "metadata": {"category": "technology", "topic": "database"}
    },
    {
      "content": "Machine learning enables computers to learn and improve from experience.",
      "metadata": {"category": "ai", "topic": "ml"}
    }
  ]'
```

2. Search for similar content:
```bash
curl -X GET "http://localhost:8080/api/search/similarity?q=Java%20web%20framework&limit=5"
```

## Configuration

The application can be configured through environment variables:

- `OPENAI_API_KEY`: Your OpenAI API key (required)
- `CHROMA_HOST`: ChromaDB host (default: localhost)
- `CHROMA_PORT`: ChromaDB port (default: 8000)
- `CHROMA_COLLECTION`: Collection name (default: spring-ai-docs)

## Docker

### Multi-stage Dockerfile

The project includes a multi-stage Dockerfile that:
1. Builds the Java application with Maven
2. Sets up Python environment for ChromaDB
3. Creates a runtime image with both Java and Python

### Building the Docker Image

```bash
docker build -t spring-ai-chroma .
```

### Running with Docker

```bash
docker run -p 8080:8080 -p 8000:8000 \
  -e OPENAI_API_KEY=your-api-key \
  -v chroma-data:/chroma/data \
  spring-ai-chroma
```

## Development

### Running Tests

```bash
./mvnw test
```

### Building the Project

```bash
./mvnw clean package
```

## Architecture

The application consists of:

- **Spring Boot Web Layer**: REST controllers for API endpoints
- **Service Layer**: Business logic for document management and search
- **ChromaDB Client**: HTTP client for communicating with ChromaDB
- **OpenAI Integration**: Service for generating embeddings
- **Configuration**: Spring configuration for beans and properties

## Technologies Used

- Spring Boot 3.2.1
- Java 17
- ChromaDB
- OpenAI API (text-embedding-ada-002)
- Docker & Docker Compose
- Maven

## License

This project is licensed under the MIT License.
