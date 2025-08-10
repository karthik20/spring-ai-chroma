#!/bin/bash

# Script to add sample documents to the ChromaDB for testing

BASE_URL="http://localhost:8080"

echo "Adding sample documents to ChromaDB..."

curl -X POST "$BASE_URL/api/search/documents" \
  -H "Content-Type: application/json" \
  -d '[
    {
      "content": "Spring Boot is a Java framework that makes it easy to create stand-alone, production-grade Spring-based applications. It provides auto-configuration, embedded servers, and production-ready features.",
      "metadata": {"category": "technology", "topic": "java", "framework": "spring"}
    },
    {
      "content": "ChromaDB is an open-source embedding database built for AI applications. It provides vector similarity search and can store embeddings alongside metadata and documents.",
      "metadata": {"category": "technology", "topic": "database", "type": "vector"}
    },
    {
      "content": "Machine learning is a method of data analysis that automates analytical model building. It enables computers to learn and improve from experience without being explicitly programmed.",
      "metadata": {"category": "ai", "topic": "machine-learning", "type": "definition"}
    },
    {
      "content": "Vector embeddings are dense vector representations of data that capture semantic meaning. They are used in natural language processing, computer vision, and recommendation systems.",
      "metadata": {"category": "ai", "topic": "embeddings", "type": "concept"}
    },
    {
      "content": "REST APIs provide a way for applications to communicate over HTTP. They use standard HTTP methods like GET, POST, PUT, and DELETE to perform operations on resources.",
      "metadata": {"category": "technology", "topic": "api", "protocol": "http"}
    },
    {
      "content": "Docker containers provide a lightweight, portable way to package applications and their dependencies. They ensure consistent deployment across different environments.",
      "metadata": {"category": "technology", "topic": "containerization", "tool": "docker"}
    },
    {
      "content": "Semantic search uses the meaning and context of search queries to provide more relevant results, rather than just matching keywords exactly.",
      "metadata": {"category": "ai", "topic": "search", "type": "semantic"}
    }
  ]'

echo -e "\n\nSample documents added successfully!"
echo "You can now test semantic search with queries like:"
echo "curl -X GET \"$BASE_URL/api/search/similarity?q=Java%20web%20framework&limit=3\""
echo "curl -X GET \"$BASE_URL/api/search/similarity?q=vector%20database&limit=3\""
echo "curl -X GET \"$BASE_URL/api/search/similarity?q=containerization%20deployment&limit=3\""