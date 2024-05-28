package com.flytrap.rssreader.api.subscribe.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flytrap.rssreader.CustomControllerTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountRoll;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.api.parser.RssSubscribeParser;
import com.flytrap.rssreader.api.parser.dto.RssSourceData;
import com.flytrap.rssreader.api.subscribe.domain.BlogPlatform;
import com.flytrap.rssreader.api.subscribe.infrastructure.implement.SubscriptionCommand;
import com.flytrap.rssreader.api.subscribe.presentation.dto.AddSubscriptionRequest;
import com.flytrap.rssreader.global.presentation.resolver.AdminAuthorizationArgumentResolver;
import com.flytrap.rssreader.global.presentation.resolver.AuthorizationArgumentResolver;
import java.util.Optional;
import javax.security.sasl.AuthenticationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomControllerTest
class SubscriptionCommandControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthorizationArgumentResolver authorizationArgumentResolver;

    @MockBean
    AdminAuthorizationArgumentResolver adminAuthorizationArgumentResolver;

    @Autowired
    FolderCommand folderCommand;

    @Autowired
    SubscriptionCommand subscriptionCommand;

    @MockBean
    RssSubscribeParser rssSubscribeParser;

    @Nested
    class 구독_추가하기_API {

        @Test
        void 구독_추가에_성공하면_201응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));

            var folder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(new AccountId(1L))
                    .build()
            );

            var request = new AddSubscriptionRequest("URL");

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/subscriptions", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.subscribeId").hasJsonPath())
                .andExpect(jsonPath("$.data.subscribeTitle").hasJsonPath());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            var request = new AddSubscriptionRequest("URL");

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/subscriptions", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void 폴더에_접근_권한이_없는_경우_403응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));

            var folder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(new AccountId(2L))
                    .build()
            );

            var request = new AddSubscriptionRequest("URL");

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/subscriptions", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }

        @Test
        void 구독을_중복으로_추가한_경우_400응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));

            var folder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(new AccountId(1L))
                    .build()
            );
            var subscription = subscriptionCommand
                .createFrom(folder.getId(), "URL");

            var request = new AddSubscriptionRequest("URL");

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/subscriptions", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class 구독_취소하기_API {

        @Test
        void 구독_취소에_성공하면_204응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));

            var folder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(new AccountId(1L))
                    .build()
            );
            var subscription = subscriptionCommand
                .createFrom(folder.getId(), "URL");

            // when, then
            mockMvc.perform(
                delete("/api/folders/{folderId}/subscriptions/{subscriptionId}",
                    folder.getId().value(),
                    subscription.getId().value()))
                .andExpect(status().isNoContent());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));

            var folder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(new AccountId(1L))
                    .build()
            );
            var subscription = subscriptionCommand
                .createFrom(folder.getId(), "URL");

            // when, then
            mockMvc.perform(
                    delete("/api/folders/{folderId}/subscriptions/{subscriptionId}",
                        folder.getId().value(),
                        subscription.getId().value()))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void 폴더에_접근_권한이_없는_경우_403응답을_반환한다() throws Exception {
            // given
            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);
            when(rssSubscribeParser.parseRssDocuments(any()))
                .thenReturn(Optional.of(
                    new RssSourceData("title", "URL", BlogPlatform.VELOG, "description")));

            var folder = folderCommand.create(
                FolderCreate.builder()
                    .name("폴더명")
                    .ownerId(new AccountId(2L))
                    .build()
            );
            var subscription = subscriptionCommand
                .createFrom(folder.getId(), "URL");

            // when, then
            mockMvc.perform(
                    delete("/api/folders/{folderId}/subscriptions/{subscriptionId}",
                        folder.getId().value(),
                        subscription.getId().value()))
                .andExpect(status().isForbidden());
        }
    }
}