package dev.inspector.springdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.inspector.springdemo.dto.SchemeBrandAccessRequestDTO;
import dev.inspector.springdemo.service.SchemeBrandAccessHandler;
import dev.inspector.springdemo.utils.FileUtils;
import dev.inspector.springdemo.utils.SetupTestBeanAndEnv;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SetupTestBeanAndEnv
public class BrandManagerSchemeControllerTest {

    private static final String SCHEME_ID = "schemeId";
    private static final String POST_BRAND_ACCESS = "/brand/scheme/" + SCHEME_ID + "/users";
    private static final String USER_ID = "userId";
    private static final String ROLE_BRAND_WEB_CUSTOMER = "ROLE_BRAND_WEB_CUSTOMER";
    private static final String JSON_PATH_ERROR_CODE = "$.error.code";
    private static final String JSON_PATH_ERROR_MSG = "$.error.message";
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchemeBrandAccessHandler schemeBrandAccessHandler;


    @Test
    @SneakyThrows
    void grantAccess_ToBrandScheme_Unauthorized() {
        mockMvc.perform(post(POST_BRAND_ACCESS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_ERROR_CODE).value("Unauthorized"))
                .andExpect(jsonPath(JSON_PATH_ERROR_MSG).value("Token is missing"));

        InOrder inOrder = Mockito.inOrder(schemeBrandAccessHandler);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @SneakyThrows
    void grantAccess_ToBrandScheme_NotVerified() {
        var request = SchemeBrandAccessRequestDTO.builder()
                .userIds(List.of(USER_ID))
                .build();
        mockMvc.perform(post(POST_BRAND_ACCESS)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("verified", false))
                                .authorities(new SimpleGrantedAuthority(ROLE_BRAND_WEB_CUSTOMER)))
                        .content(OBJECT_WRITER.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_ERROR_CODE).value("Access denied"))
                .andExpect(jsonPath(JSON_PATH_ERROR_MSG).value("Access Denied"));

        InOrder inOrder = Mockito.inOrder(schemeBrandAccessHandler);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @SneakyThrows
    void grantAccess_ToBrandScheme_WrongRole() {
        var request = SchemeBrandAccessRequestDTO.builder()
                .userIds(List.of(USER_ID))
                .build();
        mockMvc.perform(post(POST_BRAND_ACCESS)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("verified", true))
                                .authorities(new SimpleGrantedAuthority("WRONG_" + ROLE_BRAND_WEB_CUSTOMER)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_WRITER.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_ERROR_CODE).value("Access denied"))
                .andExpect(jsonPath(JSON_PATH_ERROR_MSG).value("Access Denied"));

        InOrder inOrder = Mockito.inOrder(schemeBrandAccessHandler);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @SneakyThrows
    void grantAccessToBrandScheme() {
        when(schemeBrandAccessHandler.generateSchemesForBrandAccess(anyString(), any()))
                .thenReturn(SchemeBrandAccessHandler.SchemeBrandAccessResponseDTO.builder()
                        .schemeId(SCHEME_ID)
                        .newAccessedUsers(List.of(
                                SchemeBrandAccessHandler.SchemeBrandAccessResponseDTO.BrandAccess.builder()
                                        .userId(USER_ID + "_1")
                                        .userLogin(USER_ID + "_1")
                                        .schemeId(SCHEME_ID + "_1")
                                        .build()
                        ))
                        .alreadyAccessedUsers(List.of())
                        .build());

        var request = SchemeBrandAccessRequestDTO.builder()
                .userIds(List.of(USER_ID))
                .build();
        var response = mockMvc.perform(post(POST_BRAND_ACCESS)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .jwt(jwt -> jwt.claim("verified", true))
                                .authorities(new SimpleGrantedAuthority(ROLE_BRAND_WEB_CUSTOMER)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_WRITER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONAssert.assertEquals(FileUtils.readResponseFromFile("generateSchemesForBrandAccess.json"), response, true);

        InOrder inOrder = Mockito.inOrder(schemeBrandAccessHandler);
        inOrder.verify(schemeBrandAccessHandler).generateSchemesForBrandAccess(SCHEME_ID, request);
        inOrder.verifyNoMoreInteractions();
    }

}
