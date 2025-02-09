package dev.inspector.springdemo.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
public class ErrorResponseDTO {
    private ErrorDTO error;
}
