package com.example.springaichroma.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ChromaClient {

    @Autowired
    private OkHttpClient httpClient;

    @Autowired
    private String chromaBaseUrl;

    @Autowired
    private String collectionName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Get or create a collection
     */
    public void createCollection() throws IOException {
        Map<String, Object> requestBody = Map.of(
                "name", collectionName,
                "metadata", Map.of("description", "Spring AI ChromaDB collection")
        );

        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(chromaBaseUrl + "/api/v1/collections")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // Collection might already exist, try to get it
                getCollection();
            }
        }
    }

    /**
     * Get collection information
     */
    public void getCollection() throws IOException {
        Request request = new Request.Builder()
                .url(chromaBaseUrl + "/api/v1/collections/" + collectionName)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get collection: " + response.message());
            }
        }
    }

    /**
     * Add documents to the collection
     */
    public void addDocuments(List<String> ids, List<List<Double>> embeddings, 
                            List<String> documents, List<Map<String, Object>> metadatas) throws IOException {
        Map<String, Object> requestBody = Map.of(
                "ids", ids,
                "embeddings", embeddings,
                "documents", documents,
                "metadatas", metadatas
        );

        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(chromaBaseUrl + "/api/v1/collections/" + collectionName + "/add")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to add documents: " + response.message());
            }
        }
    }

    /**
     * Query the collection for similar documents
     */
    public JsonNode queryCollection(List<List<Double>> queryEmbeddings, int nResults) throws IOException {
        Map<String, Object> requestBody = Map.of(
                "query_embeddings", queryEmbeddings,
                "n_results", nResults,
                "include", List.of("documents", "metadatas", "distances")
        );

        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(chromaBaseUrl + "/api/v1/collections/" + collectionName + "/query")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to query collection: " + response.message());
            }
            return objectMapper.readTree(response.body().string());
        }
    }

    /**
     * Check if ChromaDB is healthy
     */
    public boolean isHealthy() {
        Request request = new Request.Builder()
                .url(chromaBaseUrl + "/api/v1/heartbeat")
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }
}