package dev.inspector.springdemo.utils;

import lombok.AllArgsConstructor;
import okhttp3.mockwebserver.MockWebServer;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

@TestConfiguration
@AllArgsConstructor
public class TestConfig {

    @Bean
    public S3Client privateBucket() {
        return Mockito.mock(S3Client.class);
    }

    @Bean
    public S3Client publicBucket() {
        return Mockito.mock(S3Client.class);
    }

    @Bean
    public HetznerFileStoreProperties hetznerFileStoreProperties() {
        return new HetznerFileStoreProperties();
    }

    class HetznerFileStoreProperties{}

    @Bean(destroyMethod = "shutdown")
    public MockWebServer mockWebServer() {
        MockWebServer mockWebServer = new MockWebServer();
        try {
            mockWebServer.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start MockWebServer", e);
        }
        return mockWebServer;
    }

    @Bean
    public WebClient webClient(MockWebServer mockWebServer, WebClient.Builder builder) {
        return builder
                .baseUrl(mockWebServer.url("/").toString())
                .build();
    }
}