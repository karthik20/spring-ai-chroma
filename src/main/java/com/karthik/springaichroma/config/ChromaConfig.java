package com.karthik.springaichroma.config;

import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.ai.chroma.vectorstore.ChromaVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ChromaConfig {

    @Value("${chroma.host:localhost}")
    private String chromaHost;

    @Value("${chroma.port:8000}")
    private int chromaPort;

    @Value("${chroma.collection-name:spring-ai-docs}")
    private String collectionName;

    @Bean
    public String chromaBaseUrl() {
        return String.format("http://%s:%d", chromaHost, chromaPort);
    }

    @Bean
    public String collectionName() {
        return collectionName;
    }

    @Bean
    public RestClient.Builder builder() {
        return RestClient.builder().requestFactory(new SimpleClientHttpRequestFactory());
    }

    @Bean
    public ChromaApi chromaApi(RestClient.Builder restClientBuilder,
            @Autowired String chromaBaseUrl, @Autowired ObjectMapper objectMapper) {
        ChromaApi chromaApi = new ChromaApi(chromaBaseUrl, restClientBuilder, objectMapper);
        return chromaApi;
    }

    @Bean
    public VectorStore chromaVectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi) {
        return ChromaVectorStore.builder(chromaApi, embeddingModel)
                .collectionName("banking-docs")
                .initializeSchema(true)
                .build();
    }

}
