package dev.inspector.springdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.inspector.springdemo.dto.AuthUserData;
import dev.inspector.springdemo.service.FileProcessingService;
import dev.inspector.springdemo.service.UserSchemeMarkingService;
import dev.inspector.springdemo.utils.OauthUtils;
import dev.inspector.springdemo.utils.SetupTestBeanAndEnv;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SetupTestBeanAndEnv
class UserSchemeMarkingControllerTest {

    private static final String FILE_PUT_URL = "/schemes/scheme_1/marking";
    private static final String FILE_PATCH_URL = "/schemes/scheme_1/marking";
    private static final String USER_ID = "user_1";
    private static final String SCHEME_ID = "scheme_1";
    private static final LocalDateTime UPDATE_DATE = LocalDateTime.of(2024, 1, 1, 0, 0);
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();
    private static final MultipartFile MULTIPART_FILE =
            new MockMultipartFile("file", "test.zip", "application/json", "".getBytes());

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserSchemeMarkingService userSchemeMarkingService;
    @MockBean
    private OauthUtils oauthUtils;
    @MockBean
    private AuthUserData authUserData;
    @MockBean
    private FileProcessingService fileProcessingService;

    @BeforeEach
    void init() {
        when(authUserData.getUserId()).thenReturn(USER_ID);
        when(oauthUtils.getCurrentUser()).thenReturn(authUserData);
    }

    @Test
    @SneakyThrows
    void updateMarking() {
        var requestDto = UserSchemeMarkingService.SchemeMarkingUpdateDTO.builder()
                .stitches(createStitchesDto())
                .backstitches(createBackstitchesDto())
                .decorations(createDecorationsDto())
                .build();
        when(fileProcessingService.unzipAndConvertJsonToObject(any(MultipartFile.class), any()))
                .thenReturn(requestDto);
        when(userSchemeMarkingService.updateScheme(USER_ID, SCHEME_ID, requestDto))
                .thenReturn(UserSchemeMarkingService.SchemeMarkingResponseDTO.builder()
                        .schemeId(SCHEME_ID)
                        .version(UPDATE_DATE.toEpochSecond(ZoneOffset.UTC))
                        .build());

        mockMvc.perform(multipart(HttpMethod.PUT, FILE_PUT_URL)
                        .file("file", MULTIPART_FILE.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("verified", true))
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.schemeId").value(SCHEME_ID))
                .andExpect(jsonPath("$.version").value(UPDATE_DATE.toEpochSecond(ZoneOffset.UTC)));

        InOrder inOrder = Mockito.inOrder(oauthUtils, userSchemeMarkingService, fileProcessingService);
        inOrder.verify(fileProcessingService).unzipAndConvertJsonToObject(
                any(MultipartFile.class), eq(UserSchemeMarkingService.SchemeMarkingUpdateDTO.class));
        inOrder.verify(oauthUtils).getCurrentUser();
        inOrder.verify(userSchemeMarkingService).updateScheme(USER_ID, SCHEME_ID, requestDto);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @SneakyThrows
    void updateScheme_wrongRole() {
        var requestDto = UserSchemeMarkingService.SchemeMarkingUpdateDTO.builder()
                .stitches(createStitchesDto())
                .backstitches(createBackstitchesDto())
                .decorations(createDecorationsDto())
                .build();

        mockMvc.perform(multipart(HttpMethod.PUT, FILE_PUT_URL)
                        .file("file", MULTIPART_FILE.getBytes())
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("verified", true))
                                .authorities(new SimpleGrantedAuthority("wrong_ROLE_CUSTOMER")))
                        .content(OBJECT_WRITER.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error.code").value("Access denied"))
                .andExpect(jsonPath("$.error.message").value("Access Denied"));

        verifyNoInteractions(userSchemeMarkingService, oauthUtils);
    }

    @Test
    @SneakyThrows
    void updateScheme_verifiedFalse() {
        var requestDto = UserSchemeMarkingService.SchemeMarkingUpdateDTO.builder()
                .stitches(createStitchesDto())
                .backstitches(createBackstitchesDto())
                .decorations(createDecorationsDto())
                .build();

        mockMvc.perform(multipart(HttpMethod.PUT, FILE_PUT_URL)
                        .file("file", MULTIPART_FILE.getBytes())
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("verified", false))
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .content(OBJECT_WRITER.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error.code").value("Access denied"))
                .andExpect(jsonPath("$.error.message").value("Access Denied"));

        verifyNoInteractions(userSchemeMarkingService, oauthUtils);
    }

    @Test
    @SneakyThrows
    void updateSchemePartlyFile() {
        var requestDto = UserSchemeMarkingService.SchemeMarkingUpdateDTO.builder()
                .backstitches(List.of())
                .decorations(List.of())
                .stitches(List.of())
                .build();
        when(fileProcessingService.unzipAndConvertJsonToObject(any(MultipartFile.class), any()))
                .thenReturn(requestDto);
        when(userSchemeMarkingService.updateSchemePartly(USER_ID, SCHEME_ID, requestDto))
                .thenReturn(UserSchemeMarkingService.SchemeMarkingResponseDTO.builder()
                        .schemeId(SCHEME_ID)
                        .version(UPDATE_DATE.toEpochSecond(ZoneOffset.UTC))
                        .build());

        mockMvc.perform(multipart(HttpMethod.PATCH, FILE_PATCH_URL)
                        .file("file", MULTIPART_FILE.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("verified", true))
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schemeId").value(SCHEME_ID))
                .andExpect(jsonPath("$.version").value(UPDATE_DATE.toEpochSecond(ZoneOffset.UTC)));

        InOrder inOrder = Mockito.inOrder(userSchemeMarkingService, fileProcessingService);
        inOrder.verify(fileProcessingService).unzipAndConvertJsonToObject(
                any(MultipartFile.class), eq(UserSchemeMarkingService.SchemeMarkingUpdateDTO.class));
        inOrder.verify(userSchemeMarkingService).updateSchemePartly(USER_ID, SCHEME_ID, requestDto);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @SneakyThrows
    void updateSchemePartly_wrongRole() {
        var requestDto = UserSchemeMarkingService.SchemeMarkingUpdateDTO.builder()
                .stitches(createStitchesDto())
                .backstitches(createBackstitchesDto())
                .decorations(createDecorationsDto())
                .build();

        mockMvc.perform(multipart(HttpMethod.PATCH, FILE_PATCH_URL)
                        .file("file", MULTIPART_FILE.getBytes())
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("verified", true))
                                .authorities(new SimpleGrantedAuthority("wrong_ROLE_CUSTOMER")))
                        .content(OBJECT_WRITER.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error.code").value("Access denied"))
                .andExpect(jsonPath("$.error.message").value("Access Denied"));

        verifyNoInteractions(userSchemeMarkingService, oauthUtils);
    }

    @Test
    @SneakyThrows
    void updateSchemePartly_notVerified() {
        var requestDto = UserSchemeMarkingService.SchemeMarkingUpdateDTO.builder()
                .stitches(createStitchesDto())
                .backstitches(createBackstitchesDto())
                .decorations(createDecorationsDto())
                .build();

        mockMvc.perform(multipart(HttpMethod.PATCH, FILE_PATCH_URL)
                        .file("file", MULTIPART_FILE.getBytes())
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("verified", false))
                                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .content(OBJECT_WRITER.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error.code").value("Access denied"))
                .andExpect(jsonPath("$.error.message").value("Access Denied"));

        verifyNoInteractions(userSchemeMarkingService, oauthUtils);
    }

    private List<String> createStitches() {
        return Stream.of(
                        createStitchDTO(1),
                        createStitchDTO(2)
                )
                .map(st -> {
                    try {
                        return objectMapper.writeValueAsString(st);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private List<String> createDecorations() {
        return Stream.of(
                        createDecorationDTO(1),
                        createDecorationDTO(2)
                )
                .map(st -> {
                    try {
                        return objectMapper.writeValueAsString(st);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private List<String> createBackstitches() {
        return Stream.of(
                        createBackstitchDTO(1),
                        createBackstitchDTO(2)
                )
                .map(st -> {
                    try {
                        return objectMapper.writeValueAsString(st);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    private List<String> createStitchesDto() {
        return List.of(
                createStitchDTO(1),
                createStitchDTO(2)
        );
    }

    private List<String> createDecorationsDto() {
        return List.of(
                createDecorationDTO(1),
                createDecorationDTO(2)
        );
    }

    private List<String> createBackstitchesDto() {
        return List.of(
                createBackstitchDTO(1),
                createBackstitchDTO(2)
        );
    }

    private String createStitchDTO(int value) {
        return String.valueOf(value);
    }

    private String createBackstitchDTO(int value) {
        return String.valueOf(value);
    }

    private String createDecorationDTO(int value) {
        return String.valueOf(value);
    }
}