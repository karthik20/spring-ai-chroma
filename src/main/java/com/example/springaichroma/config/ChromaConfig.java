package com.example.springaichroma.config;

import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ChromaConfig {

    @Value("${chroma.host:localhost}")
    private String chromaHost;

    @Value("${chroma.port:8000}")
    private int chromaPort;

    @Value("${chroma.collection-name:spring-ai-docs}")
    private String collectionName;

    @Value("${openai.api-key}")
    private String openAiApiKey;

    @Bean
    public String chromaBaseUrl() {
        return String.format("http://%s:%d", chromaHost, chromaPort);
    }

    @Bean
    public String collectionName() {
        return collectionName;
    }

    @Bean
    public OkHttpClient httpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .build();
    }

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(openAiApiKey);
    }
}