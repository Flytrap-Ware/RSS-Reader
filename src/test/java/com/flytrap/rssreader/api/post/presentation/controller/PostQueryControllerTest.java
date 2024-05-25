package com.flytrap.rssreader.api.post.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

@CustomControllerTest
class PostQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorizationArgumentResolver authorizationArgumentResolver;

    @MockBean
    AdminAuthorizationArgumentResolver adminAuthorizationArgumentResolver;

    @Nested
    @Sql(scripts = "/post-sample.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    class 게시글_보기_API {

        @Test
        void 게시글_보기_요청이_성공하면_200응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(get("/api/posts/{postId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").hasJsonPath())
                .andExpect(jsonPath("$.data.guid").hasJsonPath())
                .andExpect(jsonPath("$.data.title").hasJsonPath())
                .andExpect(jsonPath("$.data.thumbnailUrl").hasJsonPath())
                .andExpect(jsonPath("$.data.description").hasJsonPath())
                .andExpect(jsonPath("$.data.pubDate").hasJsonPath())
                .andExpect(jsonPath("$.data.subscribeTitle").hasJsonPath())
                .andExpect(jsonPath("$.data.open").hasJsonPath())
                .andExpect(jsonPath("$.data.bookmark").hasJsonPath());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            // when, then
            mockMvc.perform(get("/api/posts/{postId}", 1))
                .andExpect(status().isUnauthorized());
        }

    }

}