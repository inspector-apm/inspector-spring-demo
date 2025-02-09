package dev.inspector.springdemo.controller;

import dev.inspector.springdemo.annotation.BrandManagerVerifiedAccess;
import dev.inspector.springdemo.dto.SchemeBrandAccessRequestDTO;
import dev.inspector.springdemo.service.SchemeBrandAccessHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/brand/scheme")
@BrandManagerVerifiedAccess
@RequiredArgsConstructor
public class BrandManagerSchemeController {
    private final SchemeBrandAccessHandler schemeBrandAccessHandler;


    @PostMapping(value = "/{schemeId}/users")
    public SchemeBrandAccessHandler.SchemeBrandAccessResponseDTO grantAccessToBrandScheme(
            @PathVariable String schemeId,
            @RequestBody @Valid SchemeBrandAccessRequestDTO request
    ) {
        return schemeBrandAccessHandler.generateSchemesForBrandAccess(schemeId, request);
    }
}
