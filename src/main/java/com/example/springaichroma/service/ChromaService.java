package com.example.springaichroma.service;

import com.example.springaichroma.model.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChromaService {

    private static final Logger logger = LoggerFactory.getLogger(ChromaService.class);

    @Autowired
    private ChromaClient chromaClient;

    @Autowired
    private OpenAiService openAiService;

    @Value("${openai.model:text-embedding-ada-002}")
    private String embeddingModel;

    @PostConstruct
    public void init() {
        try {
            chromaClient.createCollection();
            logger.info("ChromaDB collection initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize ChromaDB collection", e);
        }
    }

    /**
     * Search for similar documents based on query text
     */
    public List<SearchResult> searchSimilar(String query, int limit) {
        try {
            // Get embedding for the query
            List<Double> queryEmbedding = getEmbedding(query);
            
            // Query ChromaDB
            JsonNode results = chromaClient.queryCollection(List.of(queryEmbedding), limit);
            
            return parseSearchResults(results);
        } catch (Exception e) {
            logger.error("Error searching similar documents", e);
            return Collections.emptyList();
        }
    }

    /**
     * Add documents to the vector store
     */
    public void addDocuments(List<String> documents, List<Map<String, Object>> metadatas) {
        try {
            // Generate embeddings for all documents
            List<List<Double>> embeddings = documents.stream()
                    .map(this::getEmbedding)
                    .collect(Collectors.toList());

            // Generate IDs for documents
            List<String> ids = documents.stream()
                    .map(doc -> UUID.randomUUID().toString())
                    .collect(Collectors.toList());

            // Add to ChromaDB
            chromaClient.addDocuments(ids, embeddings, documents, metadatas);
            logger.info("Added {} documents to ChromaDB", documents.size());
        } catch (Exception e) {
            logger.error("Error adding documents", e);
            throw new RuntimeException("Failed to add documents", e);
        }
    }

    /**
     * Get embedding for a text using OpenAI
     */
    private List<Double> getEmbedding(String text) {
        try {
            EmbeddingRequest request = EmbeddingRequest.builder()
                    .model(embeddingModel)
                    .input(Collections.singletonList(text))
                    .build();

            List<Embedding> embeddings = openAiService.createEmbeddings(request).getData();
            if (embeddings.isEmpty()) {
                throw new RuntimeException("No embeddings returned from OpenAI");
            }

            return embeddings.get(0).getEmbedding();
        } catch (Exception e) {
            logger.error("Error getting embedding for text: {}", text, e);
            throw new RuntimeException("Failed to get embedding", e);
        }
    }

    /**
     * Parse ChromaDB query results into SearchResult objects
     */
    private List<SearchResult> parseSearchResults(JsonNode results) {
        List<SearchResult> searchResults = new ArrayList<>();
        
        try {
            JsonNode ids = results.get("ids").get(0);
            JsonNode documents = results.get("documents").get(0);
            JsonNode metadatas = results.get("metadatas").get(0);
            JsonNode distances = results.get("distances").get(0);

            for (int i = 0; i < ids.size(); i++) {
                SearchResult result = new SearchResult();
                result.setId(ids.get(i).asText());
                result.setContent(documents.get(i).asText());
                
                // Convert distance to similarity score (1 - distance)
                double distance = distances.get(i).asDouble();
                result.setScore(Math.max(0, 1.0 - distance));
                
                // Parse metadata
                JsonNode metadata = metadatas.get(i);
                Map<String, Object> metadataMap = new HashMap<>();
                if (metadata != null && !metadata.isNull()) {
                    metadata.fields().forEachRemaining(entry -> 
                        metadataMap.put(entry.getKey(), entry.getValue().asText())
                    );
                }
                result.setMetadata(metadataMap);
                
                searchResults.add(result);
            }
        } catch (Exception e) {
            logger.error("Error parsing search results", e);
        }
        
        return searchResults;
    }

    /**
     * Check if ChromaDB is healthy
     */
    public boolean isHealthy() {
        return chromaClient.isHealthy();
    }
}