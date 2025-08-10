package com.karthik.springaichroma.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.karthik.springaichroma.model.SearchResult;

@Service
public class ChromaService {

    private static final Logger logger = LoggerFactory.getLogger(ChromaService.class);

    private final VectorStore vectorStore;

    public ChromaService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<SearchResult> performSearch(String query) {
        logger.info("Performing search with query: {}", query);
        var stopwatch = new StopWatch();
        stopwatch.start();

        var documents = this.vectorStore
                .similaritySearch(SearchRequest
                        .builder()
                        .query(query)
                        .topK(3)
                        .build());
        var results = Optional.ofNullable(documents)
                .map(docs -> docs.stream()
                .map(doc -> new SearchResult(doc.getId(), doc.getText(), Optional.ofNullable(doc.getScore()).orElse(0.0), doc.getMetadata()))
                .toList())
                .orElse(List.of());

        stopwatch.stop();
        logger.info("Search completed in {} ms", stopwatch.getTotalTimeMillis());
        return results;
    }

}
