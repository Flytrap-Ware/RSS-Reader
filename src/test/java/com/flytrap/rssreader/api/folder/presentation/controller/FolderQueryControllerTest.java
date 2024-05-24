package com.flytrap.rssreader.api.folder.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flytrap.rssreader.CustomControllerTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountRoll;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.global.presentation.resolver.AdminAuthorizationArgumentResolver;
import com.flytrap.rssreader.global.presentation.resolver.AuthorizationArgumentResolver;
import javax.security.sasl.AuthenticationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@CustomControllerTest
class FolderQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthorizationArgumentResolver authorizationArgumentResolver;

    @MockBean
    AdminAuthorizationArgumentResolver adminAuthorizationArgumentResolver;

    @Nested
    class 내_폴더_조회_API {

        @Test
        void 조회_성공시_200응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(get("/api/folders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.folders.PRIVATE").hasJsonPath())
                .andExpect(jsonPath("$.data.folders.SHARED").hasJsonPath());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            // when, then
            mockMvc.perform(get("/api/folders"))
                .andExpect(status().isUnauthorized());
        }
    }
}