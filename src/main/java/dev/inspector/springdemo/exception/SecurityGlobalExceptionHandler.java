package dev.inspector.springdemo.exception;

import dev.inspector.springdemo.exception.dto.ErrorDTO;
import dev.inspector.springdemo.exception.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class SecurityGlobalExceptionHandler {

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<Object> handleOAuth2Exception(OAuth2AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        var errorDto = ErrorDTO.builder()
                .code("OAuth2 authentication failed")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(ErrorResponseDTO.of(errorDto), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        var errorDto = ErrorDTO.builder()
                .code("Access denied")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(ErrorResponseDTO.of(errorDto), HttpStatus.FORBIDDEN);
    }
}