package dev.inspector.springdemo.config;

import ch.qos.logback.core.model.processor.ProcessorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        var authorizationHeader = request.getHeader("Authorization");

        response.setContentType("application/json");
        var processErrorDto = (ProcessorException) request.getAttribute("jakarta.servlet.error.exception");
        if (authorizationHeader == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": {\"code\": \"Unauthorized\", \"message\": \"Token is missing\"}}");
        } else if (Objects.nonNull(processErrorDto)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": {\"cause\": \"" + processErrorDto.getCause()+ "\", \"message\": \"" + processErrorDto.getMessage() + "\"}}");
        } else {
            var errorDescription = (String) request.getAttribute("error.description");
            if (errorDescription == null) {
                errorDescription = "JWT token is invalid or expired";
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": {\"code\": \"Unauthorized\", \"message\": \"" + errorDescription + "\"}}");
        }
    }
}