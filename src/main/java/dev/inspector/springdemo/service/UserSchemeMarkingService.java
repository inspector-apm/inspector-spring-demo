package dev.inspector.springdemo.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public interface UserSchemeMarkingService {

    SchemeMarkingResponseDTO updateScheme(String userId, String schemeId, SchemeMarkingUpdateDTO putMarkingRequestDTO);
    SchemeMarkingResponseDTO updateSchemePartly(String userId, String schemeId, SchemeMarkingUpdateDTO putMarkingRequestDTO);

    @Data
    @Builder
    public class SchemeMarkingResponseDTO {

        private String schemeId;
        private Long version;
        private String error;
        private List<String> stitches;
        private List<String> backstitches;
        private List<String> decorations;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserSchemeMarking {

        private String id;
        private LocalDateTime createdDateTime;
        private LocalDateTime updatedDateTime;
        private String userId;
        private String schemeId;
        private List<String> stitches;
        private List<String> backstitches;
        private List<String> decorations;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class SchemeMarkingUpdateDTO {

        private List<String> stitches;
        private List<String> backstitches;
        private List<String> decorations;

    }
}
