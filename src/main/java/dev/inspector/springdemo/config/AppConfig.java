package dev.inspector.springdemo.config;

import dev.inspector.spring.interceptors.rest.RestTemplateMonitoringInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Autowired
    private RestTemplateMonitoringInterceptor restTemplateInterceptor;

    /**
     * Method that creates a RestTemplate bean.
     * In order to enable monitoring for it, we have to explicitly add {@link RestTemplateMonitoringInterceptor} as an interceptor
     * @param builder rest template builder
     * @return the rest template instance used for sending http requests
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .interceptors(restTemplateInterceptor)
                .build();
    }
}
