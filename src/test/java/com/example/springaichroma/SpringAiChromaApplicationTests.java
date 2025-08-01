package com.example.springaichroma;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "openai.api-key=test-key",
    "chroma.host=localhost",
    "chroma.port=8000"
})
class SpringAiChromaApplicationTests {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }
}