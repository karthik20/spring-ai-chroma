package com.example.springaichroma.controller;

import com.example.springaichroma.model.SearchRequest;
import com.example.springaichroma.model.SearchResult;
import com.example.springaichroma.service.ChromaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    private ChromaService chromaService;

    /**
     * Search for similar content based on semantic similarity
     */
    @PostMapping("/similarity")
    public ResponseEntity<List<SearchResult>> searchSimilar(@RequestBody SearchRequest request) {
        try {
            if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            int limit = Math.min(request.getLimit(), 10); // Max 10 results
            List<SearchResult> results = chromaService.searchSimilar(request.getQuery(), limit);
            
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Search endpoint with query parameter (GET method)
     */
    @GetMapping("/similarity")
    public ResponseEntity<List<SearchResult>> searchSimilarGet(
            @RequestParam String q,
            @RequestParam(defaultValue = "5") int limit) {
        try {
            if (q == null || q.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            int maxLimit = Math.min(limit, 10); // Max 10 results
            List<SearchResult> results = chromaService.searchSimilar(q, maxLimit);
            
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Add sample documents for testing
     */
    @PostMapping("/documents")
    public ResponseEntity<String> addDocuments(@RequestBody List<Map<String, Object>> documents) {
        try {
            List<String> contents = documents.stream()
                    .map(doc -> (String) doc.get("content"))
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> metadatas = documents.stream()
                    .map(doc -> (Map<String, Object>) doc.getOrDefault("metadata", Map.of()))
                    .collect(Collectors.toList());
            
            chromaService.addDocuments(contents, metadatas);
            return ResponseEntity.ok("Documents added successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error adding documents: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        boolean chromaHealthy = chromaService.isHealthy();
        return ResponseEntity.ok(Map.of(
                "status", chromaHealthy ? "healthy" : "unhealthy",
                "chroma", chromaHealthy ? "connected" : "disconnected"
        ));
    }
}