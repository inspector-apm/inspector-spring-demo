package dev.inspector.springdemo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.inspector.springdemo.service.UserSchemeMarkingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserSchemeMarkingServiceImpl implements UserSchemeMarkingService {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public SchemeMarkingResponseDTO updateScheme(String userId, String schemeId, SchemeMarkingUpdateDTO putMarkingRequestDTO) {

        return SchemeMarkingResponseDTO.builder()
                .schemeId(schemeId)
                .version(123L)
                .build();
    }

    @Override
    public SchemeMarkingResponseDTO updateSchemePartly(String userId, String schemeId, SchemeMarkingUpdateDTO putMarkingRequestDTO) {

        return SchemeMarkingResponseDTO.builder()
                .schemeId(schemeId)
                .version(321L)
                .build();
    }
}
