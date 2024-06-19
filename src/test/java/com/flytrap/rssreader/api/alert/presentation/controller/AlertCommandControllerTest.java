package com.flytrap.rssreader.api.alert.presentation.controller;

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
import com.flytrap.rssreader.api.alert.domain.AlertCreate;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertCommand;
import com.flytrap.rssreader.api.alert.presentation.dto.AlertRequest;
import com.flytrap.rssreader.api.auth.presentation.dto.AccountCredentials;
import com.flytrap.rssreader.api.folder.domain.FolderCreate;
import com.flytrap.rssreader.api.folder.infrastructure.implementatioin.FolderCommand;
import com.flytrap.rssreader.global.presentation.resolver.AdminAuthorizationArgumentResolver;
import com.flytrap.rssreader.global.presentation.resolver.AuthorizationArgumentResolver;
import javax.security.sasl.AuthenticationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@CustomControllerTest
class AlertCommandControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthorizationArgumentResolver authorizationArgumentResolver;

    @MockBean
    AdminAuthorizationArgumentResolver adminAuthorizationArgumentResolver;

    @Autowired
    AlertCommand alertCommand;

    @Autowired
    FolderCommand folderCommand;

    @Nested
    class 알림_등록_API {

        @Test
        void 알림_등록에_성공하면_201응답을_반환한다() throws Exception {
            // given
            var accountId = new AccountId(1L);
            var myFolder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(accountId)
                .build());

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new AlertRequest("https://discord.com/api/webhooks/webhookUrl");

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/alerts", myFolder.getId().value())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").hasJsonPath())
                .andExpect(jsonPath("$.data.platform").hasJsonPath())
                .andExpect(jsonPath("$.data.webhookUrl").hasJsonPath());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            var request = new AlertRequest("https://discord.com/api/webhooks/webhookUrl");

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/alerts", 1)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void 폴더_접근_권한이_없는_경우_403응답을_반환한다() throws Exception {
            // given
            var accountId = new AccountId(1L);
            var folder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(new AccountId(999L))
                .build());

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new AlertRequest("https://discord.com/api/webhooks/webhookUrl");

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/alerts", folder.getId().value())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        }

        @Test
        void 허용되지_않는_알림_URL_입력시_400응답을_반환한다() throws Exception {
            // given
            var accountId = new AccountId(1L);
            var myFolder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(accountId)
                .build());

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            var request = new AlertRequest("wrong webhookUrl");

            // when, then
            mockMvc.perform(post("/api/folders/{folderId}/alerts", myFolder.getId().value())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class 알림_삭제_API {

        @Test
        void 알림_삭제에_성공하면_204응답을_반환한다() throws Exception {
            // given
            var accountId = new AccountId(1L);
            var myFolder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(accountId)
                .build());
            var myAlert = alertCommand.create(new AlertCreate(
                myFolder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl01"));

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(
                delete("/api/folders/{folderId}/alerts/{alertId}",
                    myFolder.getId().value(),
                    myAlert.getId().value()))
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
            mockMvc.perform(
                    delete("/api/folders/{folderId}/alerts/{alertId}",
                        1, 1))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void 폴더_접근_권한이_없는_경우_403응답을_반환한다() throws Exception {
            // given
            var accountId = new AccountId(1L);
            var folder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(new AccountId(999L))
                .build());
            var alert = alertCommand.create(new AlertCreate(
                folder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl01"));

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(
                    delete("/api/folders/{folderId}/alerts/{alertId}",
                        folder.getId().value(),
                        alert.getId().value()))
                .andExpect(status().isForbidden());
        }
    }
}