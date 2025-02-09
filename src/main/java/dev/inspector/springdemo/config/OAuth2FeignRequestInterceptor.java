package dev.inspector.springdemo.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        var authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("internal-client")
                .principal("internal")
                .build();

        Optional.ofNullable(authorizedClientManager.authorize(authorizeRequest))
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(token -> requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getTokenValue()))
                .orElseThrow(() -> new IllegalStateException("Failed to acquire OAuth2 token for internal-client"));
    }
}