package dev.inspector.springdemo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemeBrandAccessRequestDTO {

    @NotEmpty(message = "UserIds list cannot be empty.")
    private List<String> userIds;

}
