package dev.inspector.springdemo.service.impl;

import dev.inspector.springdemo.dto.SchemeBrandAccessRequestDTO;
import dev.inspector.springdemo.service.SchemeBrandAccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchemeBrandAccessHandlerImpl implements SchemeBrandAccessHandler {

    @Override
    public SchemeBrandAccessResponseDTO generateSchemesForBrandAccess(
            String schemeId,
            SchemeBrandAccessRequestDTO request
    ) {

        return SchemeBrandAccessResponseDTO.builder()
                .schemeId(schemeId)
                .newAccessedUsers(List.of(
                        SchemeBrandAccessResponseDTO.BrandAccess.builder()
                                .userId("userId")
                                .userLogin("userLogin")
                                .schemeId("schemeId")
                                .build())
                        )
                .alreadyAccessedUsers(
                        List.of()
                )
                .build();
    }
}
