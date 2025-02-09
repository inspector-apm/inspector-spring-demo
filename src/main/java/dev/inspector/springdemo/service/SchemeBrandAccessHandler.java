package dev.inspector.springdemo.service;

import dev.inspector.springdemo.dto.SchemeBrandAccessRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public interface SchemeBrandAccessHandler {
    SchemeBrandAccessResponseDTO generateSchemesForBrandAccess(
            String schemeId,
            SchemeBrandAccessRequestDTO request
    );


    @Data
    @Builder
    public static class SchemeBrandAccessResponseDTO {

        private String schemeId;
        private List<BrandAccess> newAccessedUsers;
        private List<BrandAccess> alreadyAccessedUsers;

        @Data
        @Builder
        public static class BrandAccess {

            private String userId;
            private String userLogin;
            private String schemeId;

        }
    }
}
