package dev.inspector.springdemo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType("application/json");
        var processErrorDto = (IllegalArgumentException) request.getAttribute("jakarta.servlet.error.exception");
        if (Objects.nonNull(processErrorDto)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": {\"code\": \"" + processErrorDto.getCause()+ "\", \"message\": \"" + processErrorDto.getMessage() + "\"}}");
        } else {
            var code = "Access denied";
            var errorDescription = exception.getMessage();
            if (exception instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
                code = oAuth2AuthenticationException.getError().getErrorCode();
                errorDescription = oAuth2AuthenticationException.getError().getDescription();
            } else if (errorDescription == null) {
                errorDescription = "Credential error";
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": {\"code\": \"" + code + "\", \"message\": \"" + errorDescription + "\"}}");
        }
    }
}
