package dev.inspector.springdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.inspector.springdemo.annotation.CustomerVerifiedAccess;
import dev.inspector.springdemo.service.FileProcessingService;
import dev.inspector.springdemo.service.UserSchemeMarkingService;
import dev.inspector.springdemo.utils.OauthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/schemes")
@RequiredArgsConstructor
@CustomerVerifiedAccess
@Slf4j
public class UserSchemeMarkingController {

    private final UserSchemeMarkingService userSchemeMarkingService;
    private final OauthUtils oauthUtils;
    private final FileProcessingService fileProcessingService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @PutMapping("/{schemeId}/marking")
    public UserSchemeMarkingService.SchemeMarkingResponseDTO putSchemeFile(
            @PathVariable String schemeId,
            @RequestParam("file") MultipartFile file) {
        var markingResponseDTO = fileProcessingService.unzipAndConvertJsonToObject(file, UserSchemeMarkingService.SchemeMarkingUpdateDTO.class);
        if (Objects.isNull(markingResponseDTO.getStitches())
                || Objects.isNull(markingResponseDTO.getBackstitches())
                || Objects.isNull(markingResponseDTO.getDecorations())) {
            log.error("All marking data are null");
            throw new IllegalArgumentException("All marking data are null");
        }

        var userData = oauthUtils.getCurrentUser();
        return userSchemeMarkingService.updateScheme(userData.getUserId(), schemeId, markingResponseDTO);
    }

    @PatchMapping("/{schemeId}/marking")
    public UserSchemeMarkingService.SchemeMarkingResponseDTO patchSchemeFile(
            @PathVariable String schemeId,
            @RequestParam("file") MultipartFile file) {
        var markingResponseDTO = fileProcessingService.unzipAndConvertJsonToObject(file, UserSchemeMarkingService.SchemeMarkingUpdateDTO.class);
        List<String> stitches = markingResponseDTO.getStitches();
        List<String> backstitches = markingResponseDTO.getBackstitches();
        List<String> decorations = markingResponseDTO.getDecorations();

        if (Objects.isNull(stitches)
                && Objects.isNull(backstitches)
                && Objects.isNull(decorations)) {
            log.error("All marking data are null");
            throw new IllegalArgumentException("All data are null");
        }

        var userData = oauthUtils.getCurrentUser();
        return userSchemeMarkingService.updateSchemePartly(userData.getUserId(), schemeId, markingResponseDTO);
    }
}
