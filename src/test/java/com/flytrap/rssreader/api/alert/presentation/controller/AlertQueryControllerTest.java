package com.flytrap.rssreader.api.alert.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flytrap.rssreader.CustomControllerTest;
import com.flytrap.rssreader.api.account.domain.AccountId;
import com.flytrap.rssreader.api.account.domain.AccountRoll;
import com.flytrap.rssreader.api.alert.domain.AlertCreate;
import com.flytrap.rssreader.api.alert.domain.AlertPlatform;
import com.flytrap.rssreader.api.alert.infrastructure.implement.AlertCommand;
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
import org.springframework.test.web.servlet.MockMvc;

@CustomControllerTest
class AlertQueryControllerTest {

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
    class 알람_목록_불러오기_API {

        @Test
        void 알람_목록_불러오기_성공시_200응답을_반환한다() throws Exception {
            // given
            var accountId = new AccountId(1L);
            var myFolder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(accountId)
                .build());
            var myAlert1 = alertCommand.create(new AlertCreate(
                myFolder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl01"));
            var myAlert2 = alertCommand.create(new AlertCreate(
                myFolder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl02"));
            var myAlert3 = alertCommand.create(new AlertCreate(
                myFolder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl03"));

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(get("/api/folders/{folderId}/alerts", myFolder.getId().value()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.alerts").hasJsonPath())
                .andExpect(jsonPath("$.data.alerts").isArray());
        }

        @Test
        void 로그인_하지_않은_사용자가_요청시_401응답을_반환한다() throws Exception {
            // given
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenThrow(AuthenticationException.class);

            // when, then
            mockMvc.perform(get("/api/folders/{folderId}/alerts", 1))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void 폴더_접근_권한이_없는_경우_403응답을_반환한다() throws Exception {
            // given
            var accountId = new AccountId(1L);
            var folder = folderCommand.create(FolderCreate.builder()
                .name("폴더명")
                .ownerId(new AccountId(2L))
                .build());
            var alert1 = alertCommand.create(new AlertCreate(
                folder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl01"));
            var alert2 = alertCommand.create(new AlertCreate(
                folder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl02"));
            var alert3 = alertCommand.create(new AlertCreate(
                folder.getId(), accountId,
                AlertPlatform.DISCORD, "https://discord.com/api/webhooks/webhookUrl03"));

            var accountCredentials = new AccountCredentials(new AccountId(1L), AccountRoll.GENERAL);
            when(authorizationArgumentResolver.supportsParameter(any()))
                .thenReturn(true);
            when(authorizationArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(accountCredentials);

            // when, then
            mockMvc.perform(get("/api/folders/{folderId}/alerts", folder.getId().value()))
                .andExpect(status().isForbidden());
        }
    }
}