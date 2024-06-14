package com.flytrap.rssreader.api.post.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flytrap.rssreader.CustomControllerTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountRoll;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.post.PostScriptSql;
import com.flytrap.rssreader.api.post.domain.Bookmark;
import com.flytrap.rssreader.api.post.domain.Open;
import com.flytrap.rssreader.api.post.domain.PostAggregate;
import com.flytrap.rssreader.api.post.domain.PostId;
import com.flytrap.rssreader.api.post.infrastructure.implementation.PostCommand;
import com.flytrap.rssreader.global.presentation.resolver.AdminAuthorizationArgumentResolver;
import com.flytrap.rssreader.global.presentation.resolver.AuthorizationArgumentResolver;
import javax.security.sasl.AuthenticationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@CustomControllerTest
class PostCommandControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthorizationArgumentResolver authorizationArgumentResolver;

    @MockBean
    AdminAuthorizationArgumentResolver adminAuthorizationArgumentResolver;

    @Autowired
    PostCommand postCommand;

    @Nested
    @PostScriptSql
    class 게시글_읽음_상태_취소_API {

        @Test
        void 읽음_상태_취소_성공시_204응답을_반환한다() throws Exception {
            // given
            postCommand.updateOnlyOpen(
                PostAggregate.builder().id(new PostId("20240102000000-1")).open(Open.MARKED).build(),
                new AccountId(1L)
            );

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            mockMvc.perform(delete("/api/posts/{postId}/read", "20240101000000-1"))
                .andExpect(status().isNoContent());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            // when, then
            mockMvc.perform(delete("/api/posts/{postId}/read", 1))
                .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @PostScriptSql
    class 게시글_북마크_추가_API {

        @Test
        void 북마크_추가_성공시_201응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            mockMvc.perform(post("/api/posts/{postId}/bookmarks", "20240101000000-1"))
                .andExpect(status().isCreated());
        }


        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            // when, then
            mockMvc.perform(post("/api/posts/{postId}/bookmarks", "20240101000000-1"))
                .andExpect(status().isUnauthorized());
        }

    }

    @Nested
    @PostScriptSql
    class 게시글_북마크_취소_API {

        @Test
        void 북마크_취소_성공시_204응답을_반환한다() throws Exception {
            // given
            postCommand.updateOnlyBookmark(
                PostAggregate.builder().id(new PostId("20240102000000-1")).bookmark(Bookmark.MARKED).build(),
                new AccountId(1L)
            );

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            mockMvc.perform(delete("/api/posts/{postId}/bookmarks", "20240101000000-1"))
                .andExpect(status().isNoContent());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            // when, then
            mockMvc.perform(delete("/api/posts/{postId}/bookmarks", "20240101000000-1"))
                .andExpect(status().isUnauthorized());
        }
    }
}